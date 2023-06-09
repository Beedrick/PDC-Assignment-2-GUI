package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */
import java.sql.*;

public class CreateNewUserController {

    private CreateNewUserGUI view;
    private PopupWindow popupWindow;
    private CreateNewUser model;
    public static Connection conn;

    public CreateNewUserController() {
        this.popupWindow = new PopupWindow();
        DatabaseManager dbManager = new DatabaseManager();
        this.conn = dbManager.getConnection();
        this.model = new CreateNewUser();
    }

    public void setView(CreateNewUserGUI view) {
        this.view = view;
    }

    public boolean checkAvailable(String username) {
        // Checks if a username exists
        boolean available = false;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            String query = "SELECT COUNT(*) FROM ACCOUNTS WHERE LOWER(USERNAME) = LOWER(?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                available = (count == 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or print an error message
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return available;
    }

    public int generateUserID() throws SQLException {
        // Creates a new userID by incrementing 1 from last used userID
        int userID = 0;
        ResultSet rs = null;
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            String query = "SELECT USERID FROM ACCOUNTS ORDER BY UserID DESC FETCH FIRST 1 ROWS ONLY";
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                userID = rs.getInt("UserID"); // gets the last userID
            }
            userID++; // increment the userID by 1 for the new user
        } catch (SQLException e) {
            // Handle the exception appropriately
            System.out.println("Error occurred during userID generation: " + e.getMessage());
            throw e; // Re-throw the exception to propagate it to the caller
        } finally {
            // Close the resources in the finally block
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // Handle the exception or print an error message
                    System.out.println("Error occurred while closing ResultSet: " + e.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // Handle the exception or print an error message
                    System.out.println("Error occurred while closing Statement: " + e.getMessage());
                }
            }
        }
        return userID;
    }

    public void signup(String username, String password) {
        // Creates a new user by adding their details to the users database
        if (username.isEmpty() || password.isEmpty()) {
            popupWindow.displayPopup("Invalid input", "Please enter a valid username and password.");
            return;
        }

        if (checkAvailable(username)) {
            try {
                int userID = generateUserID();

                updateUserModel(userID, username, password);

                // Insert the new user details into the Accounts table
                String insertQuery = "INSERT INTO ACCOUNTS (USERID, USERNAME, PASSWORD) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setInt(1, userID);
                    pstmt.setString(2, username);
                    pstmt.setString(3, password);
                    pstmt.executeUpdate();
                }

                popupWindow.displayPopup("User successfully created!", "You are signed up. Go ahead and login!");
            } catch (SQLException e) {
                e.printStackTrace();
                popupWindow.displayPopup("Error occurred during signup", "An error occurred while creating the user. Please try again later.");
            }
        } else {
            popupWindow.displayPopup("OOPS!", "This username is already taken. Please enter a different username to signup.");
        }
    }

    public void updateUserModel(int userID, String username, String password) {
        // Sets unique userID, username and password for the new user
        model.setUserID(userID);
        model.setUsername(username);
        model.setPassword(password);
    }
}
