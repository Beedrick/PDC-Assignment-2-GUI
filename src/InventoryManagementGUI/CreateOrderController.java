package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateOrderController {

    private String currentUser;
    private CreateOrder[] orderArray;
    public static Connection conn;

    public CreateOrderController() {
        DatabaseManager dbManager = new DatabaseManager();
        this.conn = dbManager.getConnection();

    }

    public String getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(String currentUsername) {
        this.currentUser = currentUsername;
    }

    public int getUserID(String user) {
        // Gets userID from DB based on username
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int userID = 0;

        try {
            String query = "SELECT UserID FROM ACCOUNTS WHERE LOWER(USERNAME) = LOWER(?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                userID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    public CreateOrder[] getInventory() {
        // Returns current users inventory from DB as an Inventory array
        int currentUserID = getUserID(currentUser); // Get userID to get inventory
        int index = 0;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            String query = "SELECT * FROM INVENTORY WHERE USERID = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, currentUserID);
            ResultSet rsCountInventory = pstmt.executeQuery(); // to count inventory size

            int rowCount = getOrderSet(rsCountInventory);
            rs = pstmt.executeQuery();
            orderArray = new CreateOrder[rowCount];

            while (rs.next()) { // populate inventory objects based on SQL records
                CreateOrder inventory = new CreateOrder(
                        rs.getString("PRODUCTNAME"),
                        rs.getString("PRODUCTBRAND"),
                        rs.getDouble("PRODUCTPRICE"),
                        rs.getString("PRODUCTTYPE"),
                        rs.getInt("PRODUCTQUANTITY")
                );
                orderArray[index] = inventory;
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderArray;
    }

    public int getOrderSet(ResultSet rsCountInventory) {
        // Return size of users inventory so that can create an Inventory array with correct size
        int rowCount = 0;

        try {
            while (rsCountInventory.next()) {
                rowCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception or print an error message
        } finally {
            try {
                rsCountInventory.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception or print an error message
            }
        }
        return rowCount;
    }

}
