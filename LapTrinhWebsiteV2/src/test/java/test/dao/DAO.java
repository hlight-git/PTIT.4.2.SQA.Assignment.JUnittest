package test.dao;

import io.DBConnect;

import java.sql.*;

public class DAO {
    private static final String url = "jdbc:mysql://localhost:3306/sqa";
    private static final String user = "root";
    private static final String pass = "Hung001201023360.";
    private static Connection conn;
    public static Connection getConnection(){
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
    public static void runStatement(String statement){
        try {
            conn.prepareStatement(statement).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ResultSet doQuery(String query){
        try {
            PreparedStatement p = conn.prepareStatement(query);
            return p.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static int runAndGetResult(String statement){
        try {
            PreparedStatement p = conn.prepareStatement(statement);
            return p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void rollback(){
        runStatement("rollback;");
    }
}
