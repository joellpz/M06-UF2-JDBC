import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class NBAGeneral {
    PreparedStatement pst;
    Statement statement;

    public void resetBBDD(String path) throws IOException, SQLException {
        statement = NBAMain.c.createStatement();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            statement.execute(br.lines().collect(Collectors.joining(" \n")));
        }
        statement.close();
    }

    public void insertBaseData(String path) throws SQLException {
        List<String[]> dataList = OpenCSV.readCSV(path);
        ArrayList<String> dataContainedType = new ArrayList<>();
        Map<String, String> dataContained = new HashMap<>();

        String table = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        String sqlBase = "INSERT INTO " + table + "(";
        String aux = ") VALUES (";
        int val = 0;

        statement = NBAMain.c.createStatement();
        Map<String, String> mapType = getColumnType(table);
        //mapType.forEach((k, v) -> System.out.println(k));
        Map<String, String> foreignKeys = getForeignKeys(table);
        List <String> primaryKeys = getPrimaryKeys(table);
        //System.out.println("FK");
        //foreignKeys.keySet().forEach(System.out::println);
        //System.out.println("___________");
        for (int i = 0; i < mapType.size(); i++) {
            if (mapType.keySet().stream().toList().get(i).matches("^[0-9].+"))
                sqlBase = sqlBase.concat("\"" + mapType.keySet().stream().toList().get(i)) + "\",";
            else sqlBase = sqlBase.concat(mapType.keySet().stream().toList().get(i)) + ",";
            aux = aux.concat("?,");
        }
        sqlBase = sqlBase.substring(0, sqlBase.length() - 1).concat(aux.substring(0, aux.length() - 1).concat(");"));
        pst = NBAMain.c.prepareStatement(sqlBase);

        boolean first = true;
        for (String[] data : dataList) {
            for (int i = 0; i < data.length; i++) {
                if (first) dataContainedType.add(data[i].toLowerCase());
                else {
                    dataContained.put(dataContainedType.get(i), data[i]);
                }
            }
            if (!first) {
                //mapType.forEach((k,v) -> System.out.println(k+": "+v));
                for (int i = 1; i < mapType.keySet().size() + 1; i++) {
                    aux = mapType.keySet().stream().toList().get(i - 1);
                    if (foreignKeys.containsKey(aux)) {
                        val = makeQueryFK(aux.substring(2) + "s", aux, dataContained.get(aux));
                    }
                    System.out.println(aux+": "+ dataContained.get(aux.toLowerCase()));
                    switch (mapType.get(aux)) {
                        case "varchar" -> pst.setString(i, dataContained.get(aux.toLowerCase()));
                        case "int4" -> {
                            if (foreignKeys.containsKey(aux) && val==0 && primaryKeys.contains(aux)) {
                                insertNewLine(aux.substring(2) + "s",dataContained.get(aux));
                                i--;
                            }else if (val != 0) {
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
                                } catch (Exception e) {
                                    System.out.println("ERROR FLOAT*******************++");
                                }
                            }
                        }
                    }
                }

                try {
                    pst.execute();
                } catch (SQLException e) {
                    System.out.println(pst.toString());
                    System.out.println(e);
                    e.printStackTrace();
                    System.exit(0);
                }
            }
            /*
             INSERT INTO players (college,draftposition,born,name,
					draftyear,draftteam,position,experience,age)
					VALUES ('DUKE',25,'1968-06-24','Alaa Abdelnaby',
							1990,'Portland Trail Blazers','Power Forward',5,54)
            */
            first = false;
            dataContained.clear();
        }


    }

    private List<String> getPrimaryKeys(String table) {
        List<String> list = new ArrayList<>();
        String sql4PK = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table.toLowerCase() + "' AND CONSTRAINT_NAME LIKE '%_pkey'";
        /*
        SELECT COLUMN_NAME, UDT_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='players' AND
COLUMN_NAME NOT IN (SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE CONSTRAINT_NAME = 'players_pkey')
        * */
        try {
            ResultSet rs = statement.executeQuery(sql4PK);
            while (rs.next()) {
                list.add(rs.getString(1));
                //System.out.println(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("ERROR =" + sql4PK);
        }
        return list;
    }

    public Map<String, String> getColumnType(String table) {
        Map<String, String> mapType = new HashMap<>();
        table = table.toLowerCase();
        String sql4PK = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table + "' AND CONSTRAINT_NAME LIKE '%_pkey'";
        String sql = "SELECT COLUMN_NAME, UDT_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + table + "' AND COLUMN_NAME NOT IN (";

        String sql4FK = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table + "' AND CONSTRAINT_NAME LIKE '%_fkey'";
        /*
        SELECT COLUMN_NAME, UDT_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='players' AND
COLUMN_NAME NOT IN (SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE CONSTRAINT_NAME = 'players_pkey')
        * */
        try {
            ResultSet rs = statement.executeQuery(sql.concat(sql4PK + ")"));
            while (rs.next()) {
                mapType.put(rs.getString(1), rs.getString(2));
                //System.out.println(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("ERROR =" + sql4PK);
        }
        try {
            ResultSet rsFK = statement.executeQuery(sql4FK);
            while (rsFK.next()) {
                mapType.put(rsFK.getString(1), "int4");
                //System.out.println(rs.getString(1));
            }
            rsFK.close();
        } catch (SQLException e) {
            System.out.println("ERROR =" + sql4FK);
        }
        return mapType;
    }

    public Map<String, String> getForeignKeys(String table) {
        Map<String, String> foreignKeys = new HashMap<>();
        String sql4FK = "SELECT COLUMN_NAME, CONSTRAINT_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table.toLowerCase() + "' AND CONSTRAINT_NAME LIKE '%_fkey'";
        try {
            ResultSet rs = statement.executeQuery(sql4FK);
            while (rs.next()) {
                foreignKeys.put(rs.getString(1), rs.getString(2));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("******************Lenght: " + foreignKeys.size());
            System.out.println("ERROR GET FOREIGN KEYS =" + sql4FK);
            System.out.println(e);
        }
        return foreignKeys;
    }

    public int makeQueryFK(String table, String column, String valueToFind) {
        int id = 0;
        //System.out.println(table+valueToFind+"*******************+");
        if (column.equals("local") || column.equals("visitor") || column.equals("champion")){
            column = "idTeam";
            table = "teams";
        }

        System.out.println(column +": "+ table);
        try {
            String sql1 = "SELECT column_name FROM information_schema.columns WHERE table_name = '" + table + "' ORDER BY ordinal_position LIMIT 1 OFFSET 1";
            ResultSet rs1 = statement.executeQuery(sql1);
            String sql = "";
            while (rs1.next()) {
                sql = "SELECT " + column + " FROM " + table + " WHERE " + rs1.getString(1) + " = '" + valueToFind + "'";
            }
            //System.out.println("FK CONSULTA: " + sql);

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                id = Integer.parseInt(rs.getString(1));
                //System.out.println(id + ": " + valueToFind);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("ERROR = MAKE QUERY FK");
            System.out.println(e);
        }
        return id;
    }

    private void insertNewLine(String table, String data) {
        try {
            String sql1 = "SELECT column_name FROM information_schema.columns WHERE table_name = '" + table + "' ORDER BY ordinal_position LIMIT 1 OFFSET 1";
            ResultSet rs1 = statement.executeQuery(sql1);
            String sql = "";
            while (rs1.next()) {
                sql = "INSERT INTO " + table + "("+rs1.getString(1)+") VALUES ('" + data + "')";
            }
            //System.out.println("FK CONSULTA: " + sql);

            System.out.println(statement.execute(sql));
            rs1.close();
        } catch (SQLException e) {
            System.out.println("ERROR = MAKE QUERY FK");
            System.out.println(e);
        }
    }
}
