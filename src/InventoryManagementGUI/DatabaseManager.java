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
    private static final String URL = "jdbc:derby:InventoryDB1;";

    Connection conn;

    public DatabaseManager() {
        establishConnection();
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void establishConnection() {
        // Establish a connection to Database
        try {
            this.conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println(URL + " connected...");
        } catch (SQLException ex) {
            System.err.println(" > SQLException: " + ex.getMessage() + ex.getNextException());
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

    public void createAccountsTable() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            try (Statement statement = conn.createStatement()) {
                String createTableSQL = "CREATE TABLE ACCOUNTS("
                        + "USERID INT PRIMARY KEY, "
                        + "USERNAME VARCHAR(50) NOT NULL, "
                        + "PASSWORD VARCHAR(50) NOT NULL"
                        + ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("Table created successfully.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                // X0Y32 is the SQL state for table already exists exception in Apache Derby
                System.out.println("Table already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void createUserInventoryTable() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            try (Statement statement = conn.createStatement()) {
                String createTableSQL = "CREATE TABLE INVENTORY("
                        + "USERID INT NOT NULL, "
                        + "PRODUCTNAME VARCHAR(50) NOT NULL, "
                        + "PRODUCTBRAND VARCHAR(50) NOT NULL, "
                        + "PRODUCTPRICE DOUBLE NOT NULL, "
                        + "PRODUCTTYPE VARCHAR(50) NOT NULL, "
                        + "PRODUCTQUANTITY INTEGER NOT NULL"
                        + ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("User Inventory Table created successfully.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                // X0Y32 is the SQL state for table already exists exception in Apache Derby
                System.out.println("Table already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void createCarProductCatalogueTable() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            try (Statement statement = conn.createStatement()) {
                String createTableSQL = "CREATE TABLE CARPRODUCTCATALOGUE("
                        + "PRODUCTNAME VARCHAR(50) NOT NULL, "
                        + "PRODUCTBRAND VARCHAR(50) NOT NULL, "
                        + "PRODUCTPRICE DOUBLE NOT NULL, "
                        + "PRODUCTTYPE VARCHAR(50) NOT NULL"
                        + ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("User Inventory Table created successfully.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                // X0Y32 is the SQL state for table already exists exception in Apache Derby
                System.out.println("Table already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void createOrderInventory() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            try (Statement statement = conn.createStatement()) {
                String createTableSQL = "CREATE TABLE ORDERINVENTORY("
                        + "ORDERID INT NOT NULL, "
                        + "USERID INT NOT NULL, "
                        + "PRODUCTNAME VARCHAR(50) NOT NULL, "
                        + "PRODUCTBRAND VARCHAR(50) NOT NULL, "
                        + "PRODUCTPRICE DOUBLE NOT NULL, "
                        + "PRODUCTTYPE VARCHAR(50) NOT NULL, "
                        + "PRODUCTQUANTITY INTEGER NOT NULL"
                        + ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("ORDERINVENTORY Table created successfully.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                // X0Y32 is the SQL state for table already exists exception in Apache Derby
                System.out.println("Table already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }

}
