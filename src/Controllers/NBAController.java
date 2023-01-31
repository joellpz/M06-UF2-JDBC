package Controllers;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class NBAController {
    private Connection conn;
    private final Scanner sc = new Scanner(System.in);
//    private NBAImport nbaImport = new NBAImport();


    public NBAController(Connection connection) {
        this.conn = connection;
    }

    public void ask4Table() throws SQLException {
        System.out.print("** ¿Que tabla quieres ver de entre estas opciones? ** \n| ");
        getTableNames().forEach(s -> System.out.print(s + " | "));
        System.out.print("\nOpción: ");
        showTable(sc.nextLine().toLowerCase());
    }

    public void showTable(String table) throws SQLException {
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
    }

    public void showDataPerPlayer() throws SQLException {
        System.out.println("*** Quieres ver Partidos(P) o Temporadas (T) ***");
        System.out.println("*** Para ver ejemplo estos mostrarán datos: P -> 26, T -> 3");
        String val = sc.nextLine().toUpperCase();
        String table;
        if (val.equals("P") || val.equals("T")) {
            if (val.equals("P")) table = "playerpergame";
            else table = "playerseasons";

            showTable("players");
            System.out.println("*** De que jugador quieres ver las información, introduce el IDPLAYER? ***");
            String sql = "SELECT * FROM " + table + " WHERE idplayer ='" + sc.nextLine() + "'";
            makeQueryGetAll(sql, table);
        } else System.out.println("*** Error, Volviendo al Inicio ***");
    }

    private void makeQueryGetAll(String sql, String table) throws SQLException {
        Statement st = conn.createStatement();
        List<String> columns = getColumnsName(table);
        List<String> fk = getForeignKeys(table);
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            for (String column : columns) {
                if (fk.contains(column)) {
                    System.out.print(column + ": " + getFKValue(column.substring(2) + "s", column, rs.getString(column), 2, 2) + " | ");
                } else {
                    System.out.print(column + ": " + rs.getString(column) + " | ");
                }
            }
            System.out.println();
        }
        st.close();
        rs.close();
    }

    public String getFKValue(String table, String column, String valueToFind, int colPosToCompare, int colToGet) throws SQLException {
        Statement st = conn.createStatement();
        if (column.equals("local") || column.equals("visitor") || column.equals("champion")) {
            table = "teams";
        }
        try {

            String sql = "SELECT * FROM " + table + " WHERE " + getSecondColName(table, column, colPosToCompare).get(0) + " = '" + valueToFind + "'";

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                return rs.getString(colToGet);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("ERROR QUERY FK: " + e);
        }
        st.close();
        return null;
    }

    private List<String> getSecondColName(String table, String column, int colPosToCompare) throws SQLException {
        List<String> val = new ArrayList<>();
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
        return val;
    }

    public List<String> getColumnsName(String table) throws SQLException {
        List<String> columns = new ArrayList<>();
        Statement st = conn.createStatement();
        String sql = "SELECT * FROM " + table;
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            columns.add(resultSetMetaData.getColumnName(i));
        }
        rs.close();
        st.close();
        return columns;
    }

    private List<String> getTableNames() throws SQLException {
        List<String> tables = new ArrayList<>();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        st.close();
        rs.close();

        return tables;
    }

    public List<String> getForeignKeys(String table) throws SQLException {
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
    }

    public void insertNewData(String table) throws SQLException {
        Statement st = conn.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
        );

        List<String> fk = getForeignKeys(table);
        String column;
        boolean rep;
        System.out.println("*** Introduce la siguiente información. ***");

        String sqlBase = "SELECT * FROM " + table; //+ table;
        ResultSet rs = st.executeQuery(sqlBase);
        ResultSetMetaData rsmd = rs.getMetaData();
        rs.moveToInsertRow();
        for (int i = 2; i <= rsmd.getColumnCount(); i++) {
            column = rsmd.getColumnName(i);

            System.out.print(column);
            switch (rsmd.getColumnType(i)) {
                case Types.INTEGER -> {
                    do {

                        rep = false;
                        if (fk.contains(column) && !column.equals("idgame")) {
                            List<String> val = getSecondColName(table, column, 2);
                            System.out.print(" es una Foreign Key, introduce " + val.get(0) + " de la tabla " + val.get(1) + ": ");
                            String value = sc.nextLine();
                            rs.updateInt(column, Integer.parseInt(getFKValue(table, column, value, 2, 1)));
                        } else {
                            System.out.print(" (integer): ");
                            try {
                                rs.updateInt(column, sc.nextInt());
                            } catch (Exception e) {
                                System.out.println("*** ERROR, introduce un número ***");
                                rep = true;
                            }
                            sc.nextLine();
                        }

                    } while (rep);


                }
                case Types.FLOAT -> {
                    System.out.print(" (float): ");
                    rs.updateFloat(column, sc.nextFloat());
                    sc.nextLine();
                }
                case Types.DATE -> {
                    System.out.print(" (date yyyy-mm-dd): ");
                    rs.updateDate(column, Date.valueOf(sc.nextLine()));
                }
                case Types.VARCHAR -> {
                    System.out.print(" (varchar): ");
                    rs.updateString(column, sc.nextLine());
                }
                default -> {
                }
            }

        }
        rs.insertRow();
        rs.close();
        st.close();
    }
}
