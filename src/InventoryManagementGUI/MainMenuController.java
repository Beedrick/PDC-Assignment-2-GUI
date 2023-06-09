package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */
import java.sql.*;
import javax.swing.JOptionPane;

public class MainMenuController {

    private String currentUser;
    private MainMenuGUI view;
    private PopupWindow popupWindow;
    private Inventory[] inventoryArray;
    private CarCatalogue[] carCatalogueArray;
    public static Connection conn;

    public MainMenuController() {
        DatabaseManager dbManager = new DatabaseManager();
        this.conn = dbManager.getConnection();
        this.popupWindow = new PopupWindow();
    }

    public void setView(MainMenuGUI view) {
        this.view = view;
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

    public CarCatalogue[] getCarCatalogue() {
        // Returns the car catalogue from DB as a Car Catalogue array
        int index = 0;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            String query = "SELECT * FROM CARPRODUCTCATALOGUE";
            pstmt = conn.prepareStatement(query);
            ResultSet rsCountCatalogue = pstmt.executeQuery(); // to count car catalogue size

            int rowCount = getInventorySize(rsCountCatalogue);
            rs = pstmt.executeQuery();
            carCatalogueArray = new CarCatalogue[rowCount];

            while (rs.next()) { // populate inventory objects based on SQL records
                CarCatalogue catalogue = new CarCatalogue(
                        rs.getString("PRODUCTNAME"),
                        rs.getString("PRODUCTBRAND"),
                        rs.getDouble("PRODUCTPRICE"),
                        rs.getString("PRODUCTTYPE")
                );
                carCatalogueArray[index] = catalogue;
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carCatalogueArray;
    }

    public int getCarCatalogueSize(ResultSet rsCountCatalogue) {
        // Return size of car catalogue so that can create an Car catalogue array with correct size
        int rowCount = 0;

        try {
            while (rsCountCatalogue.next()) {
                rowCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception or print an error message
        } finally {
            try {
                rsCountCatalogue.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception or print an error message
            }
        }
        return rowCount;
    }

    public Inventory[] getInventory() {
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

            int rowCount = getInventorySize(rsCountInventory);
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

    public int getInventorySize(ResultSet rsCountInventory) {
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

    public String[] getUpdateStrings(int updateType) {
        // Returns array of strings to update the GUI for either add product/remove product/update quantity
        String[] updateStrings = new String[2];

        if (updateType == 1) {
            updateStrings[0] = "    Add a product to inventory";
            updateStrings[1] = "Add quantity: ";
        } else if (updateType == 2) {
            updateStrings[0] = "Remove a product from inventory";
            updateStrings[1] = ""; // this string will not be used its just filler to keep consistency
        } else if (updateType == 3) {
            updateStrings[0] = "Update quantity of product in your inventory";
            updateStrings[1] = "Update quantity: ";
        } else {
            updateStrings = null;
            System.out.println("Update type doesn't exist!");
        }

        return updateStrings;
    }

    public void addProduct(String productName, int quantity) {
        // Add product to DB
        int currentUserID = getUserID(currentUser);

        // Check if the quantity is negative
        if (quantity < 0) {
            String errorMessage = "Invalid quantity. Quantity cannot be negative.";

            popupWindow.displayPopup("Error", errorMessage);
            return;
        }

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

                System.out.println("ADD: Quantity updated successfully.");
            } else {
                // Find the matching product from car product catalogue
                String getProductQuery = "SELECT PRODUCTBRAND, PRODUCTPRICE, PRODUCTTYPE FROM CARPRODUCTCATALOGUE WHERE PRODUCTNAME = ?";
                PreparedStatement getProductStatement = conn.prepareStatement(getProductQuery);
                getProductStatement.setString(1, productName);

                ResultSet rsGetProduct = getProductStatement.executeQuery();

                String productBrand = "";
                double productPrice = 0.0;
                String productType = "";

                if (rsGetProduct.next()) {
                    productBrand = rsGetProduct.getString("PRODUCTBRAND");
                    productPrice = rsGetProduct.getDouble("PRODUCTPRICE");
                    productType = rsGetProduct.getString("PRODUCTTYPE");
                } else {
                    // Product not found in the catalog, display error message and return
                    String errorMessage = "Product not found.";
                    JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Product doesn't exist, insert a new row
                String insertQuery = "INSERT INTO INVENTORY (USERID, PRODUCTNAME, PRODUCTBRAND, PRODUCTPRICE, PRODUCTTYPE, PRODUCTQUANTITY) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                insertStatement.setInt(1, currentUserID);
                insertStatement.setString(2, productName);
                insertStatement.setString(3, productBrand);
                insertStatement.setDouble(4, productPrice);
                insertStatement.setString(5, productType);
                insertStatement.setInt(6, quantity);
                insertStatement.executeUpdate();

                System.out.println("ADD: New product added to inventory.");
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

                System.out.println("REMOVE: Product removed from inventory.");
            } else {
                System.out.println("REMOVE: Product not found in inventory for the current user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductQuantity(String productName, int quantity) {
        // Update quantity product to DB
        int currentUserID = getUserID(currentUser);

        try {
            // Check if the product exists in the inventory for the current user
            if (productName.isEmpty()) {
                System.out.println("UPDATE: Product name cannot be empty.");
                return;
            }

            String selectQuery = "SELECT * FROM INVENTORY WHERE USERID = ? AND PRODUCTNAME = ?";
            PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
            selectStatement.setInt(1, currentUserID);
            selectStatement.setString(2, productName);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                String updateQuery = "UPDATE INVENTORY SET PRODUCTQUANTITY = ? WHERE USERID = ? AND PRODUCTNAME = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                updateStatement.setInt(1, quantity);
                updateStatement.setInt(2, currentUserID);
                updateStatement.setString(3, productName);
                updateStatement.executeUpdate();

                System.out.println("UPDATE: Quantity updated successfully.");
            } else {
                System.out.println("UPDATE: Product not found in inventory.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyAddingInput(String productID, String quantityText) {
        // Checks if the users input is valid for updating product
        int currentUserID = getUserID(currentUser);
        //boolean verified = false;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        if (!productID.isEmpty() && !quantityText.isEmpty()) {
            if (isInteger(quantityText)) {
                try {
                    String query = "SELECT PRODUCTNAME FROM INVENTORY WHERE PRODUCTNAME = ? AND USERID = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, productID);
                    pstmt.setInt(2, currentUserID);
                    rs = pstmt.executeQuery(); // get product name

                    if (rs.next()) { // matching product name found
                        popupWindow.displayPopup("Error: invalid input!", "This product is already in your inventory");
                    } else {
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                popupWindow.displayPopup("Error: invalid input!", "Please enter a valid interger for quantity");
                return false;
            }
        } else {
            popupWindow.displayPopup("Error: invalid input!", "Please enter a valid product name and/or product quantity");
            return false;
        }

        return false;
    }

    public boolean verifyRemovingInput(String productID) {
        // Checks if the users input is valid for adding a product
        int currentUserID = getUserID(currentUser);
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        if (!productID.isEmpty()) {
            try {
                String query = "SELECT PRODUCTNAME FROM INVENTORY WHERE PRODUCTNAME = ? AND USERID = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, productID);
                pstmt.setInt(2, currentUserID);
                rs = pstmt.executeQuery(); // get product name

                if (rs.next()) { // matching product name found
                    return true;
                } else {
                    popupWindow.displayPopup("Error: invalid input!", "This product doesn't exist in your inventory");
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            popupWindow.displayPopup("Error: invalid input!", "Please enter a valid product name and/or product quantity");
            return false;
        }

        return false;
    }

    public boolean verifyUpdatingInput(String productID, String quantityText) {
        // Checks if the users input is valid for updating product
        int currentUserID = getUserID(currentUser);
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        if (!productID.isEmpty() && !quantityText.isEmpty()) {
            if (isInteger(quantityText)) {
                try {
                    String query = "SELECT PRODUCTNAME FROM INVENTORY WHERE PRODUCTNAME = ? AND USERID = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, productID);
                    pstmt.setInt(2, currentUserID);
                    rs = pstmt.executeQuery(); // get product name

                    if (rs.next()) { // matching product name found
                        return true;
                    } else {
                        popupWindow.displayPopup("Error: invalid input!", "This product doesn't exist in your inventory");
                        return false;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                popupWindow.displayPopup("Error: invalid input!", "Please enter a valid interger for quantity");
                return false;
            }
        } else {
            popupWindow.displayPopup("Error: invalid input!", "Please enter a valid product name and/or product quantity");
            return false;
        }

        return false;
    }

    public boolean isInteger(String input) {
        // Checks if string is an int
        try {

            if (Integer.parseInt(input) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
