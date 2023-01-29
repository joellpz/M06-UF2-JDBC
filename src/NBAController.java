import com.sun.jdi.PrimitiveValue;

import java.sql.*;
import java.util.*;

public class NBAController {
    private Connection conn;

    public NBAController(Connection connection) {
        this.conn = connection;
    }

    public void showTable() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("** ¿Que tabla quieres ver de entre estas opciones? ** \n| ");
        getTableNames().forEach(s -> System.out.print(s + " | "));
        System.out.print("\nOpción: ");
        String table = sc.nextLine().toLowerCase();

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

    private List<String> getColumnsName(String table) throws SQLException {
        List<String> columns = new ArrayList<>();
        Statement st = conn.createStatement();
        String sql = "SELECT * FROM " + table;
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            columns.add(resultSetMetaData.getColumnName(i));
        }
        /*while (rs.next()) {
            columns.add(rs.getString(1));
        }*/
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
}
