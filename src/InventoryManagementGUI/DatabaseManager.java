package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String USERNAME = "gui"; 
    private static final String PASSWORD = "gui"; 
    private static final String URL = "jdbc:derby://localhost:1527/InventoryDB;";
    Connection conn;

    public DatabaseManager() {
        establishConnection();
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void establishConnection() {
        // Establish a connection to Database
        try{
            this.conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println(URL + " connected...");
        }
        catch (SQLException ex) {
            System.err.println(" > SQLException: " + ex.getMessage());
        }  
    }

    public void closeConnections() {
    // Closes connection to the database
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public ResultSet queryDB(String sql) {
        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultSet;
    }
    

    public void updateDB(String sql) {
        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
