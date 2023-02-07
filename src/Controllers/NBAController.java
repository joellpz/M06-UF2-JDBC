package Controllers;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Controlador con métodos compartidos entre controladores.
 */
public class NBAController {
    private final Connection conn;
    private final Scanner sc = new Scanner(System.in);

    /**
     * Define la conexión del controlador
     *
     * @param connection Data Base Connection
     */
    public NBAController(Connection connection) {
        this.conn = connection;
    }

    /**
     * Método que solicita la tabla a mostrar.
     */
    public void ask4Table() {
        System.out.print("** ¿Que tabla quieres ver de entre estas opciones? ** \n| ");
        getTableNames().forEach(s -> System.out.print(s + " | "));
        System.out.print("\nOpción: ");
        showTable(sc.nextLine().toLowerCase());
    }

    /**
     * Muestra todas las entradas de una tabla
     *
     * @param table Tabla a mostrar
     */
    public void showTable(String table) {
        try {
            List<String> columns = getColumnsName(table);
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM " + table);
            while (rs.next()) {
                for (String column : columns) {
                    System.out.print(column + ": " + rs.getString(column) + " | ");
                }
                System.out.println();
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
    }


    /**
     * Realiza la consulta relacionadas con las tablas intermedias,
     * solicita filtros para realizar las consultas.
     *
     * @param table Tabla
     */
    public void showDataPerPlayer(String table) {
        showTable("players");
        System.out.println("*** Para ver ejemplo estos mostrarán datos: P -> Bam Adebayo, T -> Kareem Abdul-Jabbar");
        System.out.println("*** De que jugador quieres ver las información, introduce el nombre del jugador? ***");
        String sql = "SELECT * FROM " + table + " WHERE idplayer ='" + getFKValue("players", "name", sc.nextLine(), 2, 1) + "'";
        makeQueryGetAll(sql, table);
    }


    /**
     * Muestra la informacion contenida dentro de las tablas intermedias.
     *
     * @param sql   Consulta
     * @param table Tabla
     */
    private void makeQueryGetAll(String sql, String table) {
        try {
            Statement st = conn.createStatement();
            List<String> columns = getColumnsName(table);
            List<String> fk = getForeignKeys(table);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                for (String column : columns) {
                    if (fk.contains(column)) {
                        System.out.print(column + ": " + getFKValue(column.substring(2) + "s", column, rs.getString(column), 1, 2) + " | ");
                    } else {
                        System.out.print(column + ": " + rs.getString(column) + " | ");
                    }
                }
                System.out.println();
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
    }

    /**
     * Realiza la consulta dentro de otra tabla cuando se realizan peticiones sobre una foreign key.
     *
     * @param table           tabla
     * @param column          columna
     * @param valueToFind     valor a encontrar
     * @param colPosToCompare columna a comparar
     * @param colToGet        columna a recoger
     * @return Respuesta de la consulta
     */
    public String getFKValue(String table, String column, String valueToFind, int colPosToCompare, int colToGet) {
        try {
            Statement st = conn.createStatement();
            if (column.equals("local") || column.equals("visitor") || column.equals("champion")) {
                table = "teams";
            }
            try {
                String sql = "SELECT * FROM " + table + " WHERE " + getColName(table, column, colPosToCompare).get(0) + " = '" + valueToFind + "'";
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    return rs.getString(colToGet);
                }
                rs.close();
            } catch (SQLException e) {
                System.out.println("ERROR QUERY FK: " + e);
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
            System.out.println("** Alomejor el jugador no existe! **");
        }
        return null;
    }

    /**
     * Obtener el nombre de la columna en alguna posición para realizar posteriormente comparaciones.
     *
     * @param table           Tabla
     * @param column          Columna (no siempre necessaria)
     * @param colPosToCompare Posición de la columna a recoger.
     * @return List de Strings que en la primera posición
     */
    private List<String> getColName(String table, String column, int colPosToCompare) {
        List<String> val = new ArrayList<>();
        try {
            if (column.equals("local") || column.equals("visitor") || column.equals("champion")) {
                table = "teams";
            }
            Statement st = conn.createStatement();
            String sql1 = "SELECT column_name FROM information_schema.columns WHERE table_name = '" + table + "' ORDER BY ordinal_position LIMIT 1 OFFSET " + (colPosToCompare - 1);
            ResultSet rs = st.executeQuery(sql1);
            while (rs.next()) {
                val.add(rs.getString(1));
            }
            val.add(table);
        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
        return val;
    }

    /**
     * Recoge el nombre de todas las columnas
     *
     * @param table Tabla
     * @return Lista de Strings que contiene el nombre de las columnas
     */
    public List<String> getColumnsName(String table) {
        List<String> columns = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM " + table;
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                columns.add(resultSetMetaData.getColumnName(i));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
        return columns;
    }

    /**
     * Recoge todos lo nombres de las tablas de la BBDD
     *
     * @return Lista de todas las Tablas de la BBDD
     */
    private List<String> getTableNames() {
        List<String> tables = new ArrayList<>();
        try {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            st.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
        return tables;
    }

    /**
     * Recoge las columnas que son Foreign Keys para poder realizar consultas cruzadas.
     *
     * @param table Tabla
     * @return Lista de FK
     */
    public List<String> getForeignKeys(String table) {
        try {
            Statement st = conn.createStatement();
            List<String> foreignKeys = new ArrayList<>();
            String sql4FK = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='" + table.toLowerCase() + "' AND CONSTRAINT_NAME LIKE '%_fkey'";
            try {
                ResultSet rs = st.executeQuery(sql4FK);
                while (rs.next()) {
                    foreignKeys.add(rs.getString(1));
                }
                rs.close();
            } catch (SQLException e) {
                System.out.println("ERROR GETTING FKEYS :" + e);
            }
            st.close();
            return foreignKeys;
        } catch (
                SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
        return null;
    }

    /**
     * Realiza la funcion de insertar datos en la BBDD
     *
     * @param table Tabla
     */
    public void insertNewData(String table) {
        try {
            Statement st = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            String sqlBase = "SELECT * FROM " + table; //+ table;
            ResultSet rs = st.executeQuery(sqlBase);
            System.out.println("*** INSERTAR ***");
            rs.moveToInsertRow();

            newData(table, rs, null);


            rs.insertRow();
            rs.close();
            st.close();

            System.out.println(" *** INSERTADO *** ");
        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
    }

    /**
     * Realiza la actualizacion de datos de una fila en alguna tabla.
     *
     * @param table Tabla
     */
    public void updateData(String table) {
        try {
            System.out.println("*** ACTUALIZAR ***");
            System.out.println("*** Qué columna quieres comparar: ***");
            List<String> columns = getColumnsName(table);
            for (int i = 0; i < columns.size(); i++) {
                System.out.println((i + 1) + ": " + columns.get(i));
            }
            String column = columns.get(sc.nextInt() - 1);
            sc.nextLine();
            System.out.println("*** Qué valor quieres buscar: ***");
            String sqlBase = "SELECT * FROM " + table + " WHERE " + column + " = '" + sc.nextLine() + "'"; //+ table;
            Statement st = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            ResultSet rs = st.executeQuery(sqlBase);
            rs.last();
            if (rs.getRow() == 0) {
                System.out.println(" ** NO se ha encontrado ningún resultado con tu búsqueda ** ");
            } else {
                rs.beforeFirst();
                while (rs.next()) {
                    System.out.println("*** Que columna quieres actualizar? ***");
                    for (int i = 1; i < columns.size(); i++) {
                        System.out.println(i + ": " + columns.get(i));
                    }
                    column = columns.get(sc.nextInt());
                    sc.nextLine();
                    newData(table, rs, column);
                    rs.updateRow();
                }
                System.out.println(" *** ACTUALIZADO ***");
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e + " !!");
        }
    }

    /**
     * Realiza una eliminación de las filas que coincidan con el filtro introducido.
     *
     * @param table Tabla
     */
    public void deleteData(String table) {
        try {
            Statement st = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            System.out.println("*** ELIMINAR ***");
            System.out.println("*** Qué columna quieres comparar: ***");
            List<String> columns = getColumnsName(table);
            for (int i = 0; i < columns.size(); i++) {
                System.out.println((i + 1) + ": " + columns.get(i));
            }
            String columnToFind = columns.get(sc.nextInt() - 1);
            sc.nextLine();
            System.out.println("*** Qué valor quieres buscar (se eliminaran todas las finals que contengan este valor): ***");
            String valueToFind = sc.nextLine();
            System.out.println("*** Qué comparador quieres utilizar en este caso (<, >, = , !=)? ***");
            String sqlBase = "SELECT * FROM " + table + " WHERE " + columnToFind + sc.nextLine() + "'" + valueToFind + "'"; //+ table;
            ResultSet rs = st.executeQuery(sqlBase);
            rs.last();
            if (rs.getRow() == 0) {
                System.out.println(" ** NO se ha encontrado ningún resultado con tu búsqueda ** ");
            } else {
                rs.beforeFirst();
                while (rs.next()) {
                    rs.deleteRow();
                }
            }

            System.out.println(" *** ELIMINADO ***");
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }

    }

    /**
     * Metodo que permite formatear de manera correcta la información
     * a la hora de introducir los datos tanto en insertar como en actualizar.
     *
     * @param table String
     * @param rs    ResultSet de la Consulta
     */
    private void newData(String table, ResultSet rs, String updateColumn) {
        try {
            List<String> fk = getForeignKeys(table);
            ResultSetMetaData rsmd = rs.getMetaData();
            boolean rep;
            String column;
            System.out.println("*** Introduce la siguiente información. ***");
            int ini = 2;
            int colNum;
            if ((rsmd.getTableName(1).equals("playerseasons") || rsmd.getTableName(1).equals("playerpergame") || rsmd.getTableName(1).equals("teamperseason")))
                ini = 1;
            if (updateColumn != null) {
                colNum = 2;
            } else colNum = rsmd.getColumnCount();
            for (int i = ini; i <= colNum; i++) {
                if (updateColumn != null) {
                    column = updateColumn;
                } else column = rsmd.getColumnName(i);
                try {
                    System.out.print(column);
                    switch (rsmd.getColumnType(i)) {
                        case Types.INTEGER -> {
                            if (fk.contains(column) && !column.equals("idgame")) {
                                List<String> val = getColName(column.substring(2) + "s", column, 2);
                                System.out.print(" es una Foreign Key, introduce " + val.get(0) + " de la tabla " + val.get(1) + ": ");
                                String value = sc.nextLine();
                                rs.updateInt(column, Integer.parseInt(getFKValue(column.substring(2) + "s", column, value, 2, 1)));
                            } else {
                                System.out.print(" (integer): ");

                                rs.updateInt(column, Integer.parseInt(sc.nextLine()));
                            }
                        }

                        case Types.FLOAT, Types.REAL -> {
                            System.out.print(" (float): ");
                            rs.updateFloat(column, Float.parseFloat(sc.nextLine()));

                        }
                        case Types.DATE -> {
                            System.out.print(" (date yyyy-mm-dd): ");
                            rs.updateDate(column, Date.valueOf(sc.nextLine()));
                        }
                        case Types.VARCHAR -> {
                            if (column.equals("years")) System.out.println(" (Ex. 1990-91)");
                            System.out.print(" (varchar): ");
                            rs.updateString(column, sc.nextLine());
                        }
                        case Types.OTHER -> {
                            //rs.updateObject(column, "'00:00:00'", Types.OTHER);
                        }
                    }
                } catch (Exception numException) {
                    System.out.println("** !Error en el formato! **");
                    i--;
                }
            }
        } catch (
                SQLException e) {
            System.out.println("¡¡ ERROR -> " + e);
        }
    }
}
