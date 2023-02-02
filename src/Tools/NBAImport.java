package Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Clase que permite importar CSV e introducirlo en la BBDD.
 */
public class NBAImport {
    PreparedStatement pst;
    Statement statement;
    Connection c;

    /**
     * Public Constructor
     * @param connection Conexión
     */
    public NBAImport(Connection connection) {
        this.c = connection;
    }

    /**
     * Metodo para reconstruir la Base de Datos.
     */
    public void resetDataBase() {
        executeSQLScript("data/EER/nba-db-postgresql.sql");
        Scanner sc = new Scanner(System.in);
        System.out.println("*** ¿Quieres Importar los datos de los CSV (S/N)? ***");
        if (sc.nextLine().equalsIgnoreCase("s")) {
            importData("data/CSV/players.csv");
            importData("data/CSV/teams.csv");
            importData("data/CSV/seasons.csv");
            importData("data/CSV/games.csv");

            importData("data/CSV/playerSeasons.csv");
            importData("data/CSV/playerPerGame.csv");
            importData("data/CSV/teamPerSeason.csv");
        }
    }

    /**
     * Permite reiniciar la BBDD determinada ejecutando desde 0 un Scirpt SQL.
     *
     * @param path URI SQL Script
     */
    public void executeSQLScript(String path) {
        try {
            System.out.println("*** Reiniciando BBDD ***");
            statement = c.createStatement();
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                statement.execute(br.lines().collect(Collectors.joining(" \n")));
            }
            statement.close();
            System.out.println("*** Done! ***");
        } catch (
                SQLException | IOException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
    }

    /**
     * Funcion que introduce los datos de un CSV a la base de datos. Los archivos deben tener el nombre de las columnas en la parte superior.
     *
     * @param path CSV with data
     */
    public void importData(String path) {
        try {
            List<String[]> dataList = OpenCSV.readCSV(path);
            ArrayList<String> dataContainedType = new ArrayList<>();
            Map<String, String> dataContained = new HashMap<>();

            String table = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
            String sqlBase = "INSERT INTO " + table + "(";
            String aux = ") VALUES (";
            int val;

            System.out.println("*** Insertando datos en " + table + " ***");

            statement = c.createStatement();
            Map<String, String> mapType = getColumnType(table);
            List<String> foreignKeys = getForeignKeys(table);
            foreignKeys.remove("idgame");
            List<String> primaryKeys = getPrimaryKeys(table);

            for (int i = 0; i < mapType.size(); i++) {
                if (mapType.keySet().stream().toList().get(i).matches("^[0-9].+"))
                    sqlBase = sqlBase.concat("\"" + mapType.keySet().stream().toList().get(i)) + "\",";
                else sqlBase = sqlBase.concat(mapType.keySet().stream().toList().get(i)) + ",";
                aux = aux.concat("?,");
            }
            sqlBase = sqlBase.substring(0, sqlBase.length() - 1).concat(aux.substring(0, aux.length() - 1).concat(");"));
            pst = c.prepareStatement(sqlBase);

            boolean first = true;
            for (String[] data : dataList) {
                for (int i = 0; i < data.length; i++) {
                    if (first) dataContainedType.add(data[i].toLowerCase());
                    else {
                        dataContained.put(dataContainedType.get(i), data[i]);
                    }
                }
                if (!first) {
                    for (int i = 1; i < mapType.keySet().size() + 1; i++) {
                        aux = mapType.keySet().stream().toList().get(i - 1);
                        val = 0;
                        if (foreignKeys.contains(aux)) {
                            val = makeQueryFK(aux.substring(2) + "s", aux, dataContained.get(aux));
                        }
                        switch (mapType.get(aux)) {
                            case "varchar" -> pst.setString(i, dataContained.get(aux.toLowerCase()));
                            case "int4" -> {
                                if (foreignKeys.contains(aux) && val == 0 && primaryKeys.contains(aux)) {
                                    insertNewLine(aux.substring(2) + "s", dataContained.get(aux));
                                    i--;
                                } else if (val != 0) {
                                    pst.setInt(i, val);
                                } else {
                                    if (dataContained.get(aux.toLowerCase()).replaceAll("[^0-9]", "").equals("")) {
                                        pst.setNull(i, java.sql.Types.INTEGER);
                                    } else if (dataContained.get(aux.toLowerCase()) == null)
                                        System.out.println("null " + dataContained.get(aux.toLowerCase()));
                                    else
                                        pst.setObject(i, Integer.parseInt(dataContained.get(aux.toLowerCase()).replaceAll("[^0-9]", "")));
                                }
                            }
                            case "date" -> {
                                if (dataContained.get(aux.toLowerCase()) == null) {
                                    pst.setNull(i, Types.DATE);
                                } else if (dataContained.get(aux.toLowerCase()).length() == 4) {
                                    pst.setDate(i, Date.valueOf(dataContained.get(aux.toLowerCase()) + "-01-01"));
                                } else {
                                    if (!dataContained.get(aux.toLowerCase()).matches("\\d+-\\d+-\\d+")) {
                                        try {
                                            pst.setDate(i, Date.valueOf(new SimpleDateFormat("yyyy-MM-dd")
                                                    .format(new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.ENGLISH)
                                                            .parse(dataContained.get(aux.toLowerCase())))));
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else {
                                        pst.setDate(i, Date.valueOf(dataContained.get(aux.toLowerCase())));
                                    }
                                }
                            }
                            case "float4" -> {
                                if (dataContained.get(aux.toLowerCase()).equals("none")) pst.setObject(i, null);
                                else {
                                    try {
                                        pst.setObject(i, Float.parseFloat(dataContained.get(aux.toLowerCase())));
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                            case "interval" -> {
                                if (dataContained.get(aux.toLowerCase()).matches("\\d+:\\d+"))
                                    pst.setObject(i, "00:" + dataContained.get(aux.toLowerCase()), Types.OTHER);
                                else pst.setObject(i, "'00:00:00'", Types.OTHER);
                            }
                        }
                    }
                    try {
                        pst.execute();
                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                }
                first = false;
                dataContained.clear();
            }
            statement.close();
            System.out.println("*** Done! ***");
        } catch (
                SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
    }

    /**
     * Funciona que recoge las Primary Keys de la Tabla indicada.
     *
     * @param table Nombre de la Tabla
     * @return Lista de las PrimaryKeys
     */
    private List<String> getPrimaryKeys(String table) {
        List<String> list = new ArrayList<>();
        String sql4PK = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table.toLowerCase() + "' AND CONSTRAINT_NAME LIKE '%_pkey'";
        try {
            ResultSet rs = statement.executeQuery(sql4PK);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return list;
    }

    /**
     * Recoge el nombre de la columna juntamente con el tipo de dato que tiene que introducir.
     *
     * @param table Tabla
     * @return Map Columna, Tipo de Dato
     */
    public Map<String, String> getColumnType(String table) {
        Map<String, String> mapType = new HashMap<>();
        table = table.toLowerCase();

        String sql4PK = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table + "' AND CONSTRAINT_NAME LIKE '%_pkey'";
        String sql4FK = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table + "' AND CONSTRAINT_NAME LIKE '%_fkey'";
        String sql = "SELECT COLUMN_NAME, UDT_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + table + "' AND COLUMN_NAME NOT IN (";

        try {
            ResultSet rs = statement.executeQuery(sql.concat(sql4PK + ")"));
            while (rs.next()) {
                mapType.put(rs.getString(1), rs.getString(2));
            }
            rs.close();

            ResultSet rsFK = statement.executeQuery(sql4FK);
            while (rsFK.next()) {
                mapType.put(rsFK.getString(1), "int4");
            }
            rsFK.close();
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return mapType;
    }

    /**
     * Enumera las foreign keys de la tabla definida, y las obtiene juntamente con su nombre de Contraint.
     *
     * @param table Tabla
     * @return Map Nombre de Columna,Noombre de la Constraint
     */
    public List<String> getForeignKeys(String table) {
        List<String> foreignKeys = new ArrayList<>();
        String sql4FK = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table.toLowerCase() + "' AND CONSTRAINT_NAME LIKE '%_fkey'";
        try {
            ResultSet rs = statement.executeQuery(sql4FK);
            while (rs.next()) {
                foreignKeys.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("ERROR GETTING FKEYS :" + e);
        }
        return foreignKeys;
    }

    /**
     * Realiza la consulta para recoger y relacionar los atributos que son ForeignKeys.
     * Assignando el id correcto según el valor a buscar.
     *
     * @param table       Tabla en la que buscar.
     * @param column      Columna que queremos recoger.
     * @param valueToFind Valor a buscar.
     * @return  int
     */
    public int makeQueryFK(String table, String column, String valueToFind) {
        if (column.equals("local") || column.equals("visitor") || column.equals("champion")) {
            column = "idTeam";
            table = "teams";
        }
        try {
            String sql1 = "SELECT column_name FROM information_schema.columns WHERE table_name = '" + table + "' ORDER BY ordinal_position LIMIT 1 OFFSET 1";
            ResultSet rs1 = statement.executeQuery(sql1);
            String sql = "";
            while (rs1.next()) {
                sql = "SELECT " + column + " FROM " + table + " WHERE " + rs1.getString(1) + " = '" + valueToFind + "'";
            }
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                return Integer.parseInt(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("ERROR QUERY FK: " + e);
        }
        return 0;
    }

    /**
     * Inserta en la tabla indicada un campo en la 2nda columna de la misma,
     * como solución a no haber recogido la información de todos los jugadores
     * y equipos durante el WebScrapping.
     *
     * @param table Table Name
     * @param data  Info TO INSERT
     */
    private void insertNewLine(String table, String data) {
        System.out.println("*** " + data + " no encontrado en '" + table + "', insertando... *** ");
        try {
            String sql1 = "SELECT column_name FROM information_schema.columns WHERE table_name = '" + table + "' ORDER BY ordinal_position LIMIT 1 OFFSET 1";
            ResultSet rs1 = statement.executeQuery(sql1);
            String sql = "";
            while (rs1.next()) {
                sql = "INSERT INTO " + table + "(" + rs1.getString(1) + ") VALUES ('" + data + "')";
            }

            statement.execute(sql);
            rs1.close();
        } catch (SQLException e) {
            System.out.println("ERROR QUERY INSERT: " + e);
        }
    }
}
