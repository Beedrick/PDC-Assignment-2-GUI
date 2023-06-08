package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */

import java.sql.*;
import java.sql.ResultSetMetaData;


public class MainMenuController {
    
    private String currentUser;
    private MainMenuGUI view;
    private PopupWindow popupWindow;
    private Inventory[] inventoryArray;
    public static Connection conn;  

    public MainMenuController() {
        DatabaseManager dbManager = new DatabaseManager();
        this.conn = dbManager.getConnection();
        //this.model = new Inventory();
    }

    public void setView(MainMenuGUI view) {
        this.view = view;
    }
    
    public String getCurrentUser(){
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
    
    public Inventory[] getInventory() {
    // Returns current users inventory from DB as an Inventory array
        int currentUserID = getUserID(currentUser); // Get userID to get inventory
        System.out.println("UserID: " + currentUserID);
        int index = 0;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try { 
            String query = "SELECT * FROM INVENTORY WHERE USERID = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, currentUserID);
            ResultSet rsCount = pstmt.executeQuery(); // to count inventory size

            int rowCount = getInventorySize(rsCount);
            rs = pstmt.executeQuery();
            inventoryArray = new Inventory[rowCount];

            while (rs.next()) { // populate inventory objects based on SQL records
                Inventory inventory = new Inventory(
                    rs.getString("PRODUCTNAME"),
                    rs.getString("PRODUCTBRAND"),
                    rs.getDouble("PRODUCTPRICE"),
                    rs.getString("PRODUCTTYPE"), 
                    rs.getInt("PRODUCTQUANTITY")
                );       
                inventoryArray[index] = inventory;
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return inventoryArray;
    }

    public int getInventorySize(ResultSet rsCount) {
    // Return size of users inventory so that can create an Inventory array with correct size
        int rowCount = 0;

        try {
            while (rsCount.next()) {
                rowCount++;
            }
        } catch (SQLException e) {
            // Handle the exception or print an error message
            e.printStackTrace();
        } finally {
            try {
                rsCount.close();
            } catch (SQLException e) {
                // Handle the exception or print an error message
                e.printStackTrace();
            }
        }

        return rowCount;
    }

    public String[] getUpdateStrings(int updateType) {
    // Returns array of strings to update the GUI for either add product/remove product/update quantity
        String[] updateStrings = new String[3];

        if (updateType == 1) {
            updateStrings[0] = "    Add a product to inventory";
            updateStrings[1] = "Add quantity: ";
            updateStrings[2] = "add product(s)";
        } 
        else if (updateType == 2) {
            updateStrings[0] = "Remove a product from inventory";
            updateStrings[1] = "NO QUANTITY"; // this string will not be use its just filler to keep consistency
            updateStrings[2] = "remove product";
        }
        else if (updateType == 3) {
            updateStrings[0] = "Update quantity of product in your inventory";
            updateStrings[1] = "Update quantity: ";
            updateStrings[2] = "update quantity";
        }
        else {
            System.out.println("Update type doesn't exist!");
        }

        return updateStrings;
    }

    public void addProduct(String productName, int quantity) {
    // Add product to DB
        int currentUserID = getUserID(currentUser);

        try {
            // Check if the product exists in the inventory for the current user
            String selectQuery = "SELECT * FROM INVENTORY WHERE USERID = ? AND PRODUCTNAME = ?";
            PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
            selectStatement.setInt(1, currentUserID);
            selectStatement.setString(2, productName);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                // Product exists, update the quantity
                int currentQuantity = rs.getInt("PRODUCTQUANTITY");
                int updatedQuantity = currentQuantity + quantity;

                String updateQuery = "UPDATE INVENTORY SET PRODUCTQUANTITY = ? WHERE USERID = ? AND PRODUCTNAME = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                updateStatement.setInt(1, updatedQuantity);
                updateStatement.setInt(2, currentUserID);
                updateStatement.setString(3, productName);
                updateStatement.executeUpdate();

                System.out.println("Quantity updated successfully.");
            } else {
                // Product doesn't exist, insert a new row
                String insertQuery = "INSERT INTO INVENTORY (USERID, PRODUCTNAME, PRODUCTQUANTITY) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                insertStatement.setInt(1, currentUserID);
                insertStatement.setString(2, productName);
                insertStatement.setInt(3, quantity);
                insertStatement.executeUpdate();

                System.out.println("New product added to inventory.");
            }

            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeProduct(String productName) {
    // Remove product to DB
        int currentUserID = getUserID(currentUser);

        try {
            // Check if the product exists in the inventory for the current user
            String selectQuery = "SELECT * FROM inventory WHERE UserID = ? AND ProductName = ?";
            PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
            selectStatement.setInt(1, currentUserID);
            selectStatement.setString(2, productName);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                // Product exists, delete the row
                String deleteQuery = "DELETE FROM inventory WHERE UserID = ? AND ProductName = ?";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, currentUserID);
                deleteStatement.setString(2, productName);
                deleteStatement.executeUpdate();

                System.out.println("Product removed from inventory.");
            } else {
                System.out.println("Product not found in inventory for the current user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
