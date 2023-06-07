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
        int count = 0;

        try { 
            String query = "SELECT COUNT(*) FROM ACCOUNTS WHERE LOWER(USERNAME) = LOWER(?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                available = false;
            } else {
                available = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            System.out.println("Error occurred during userID generation");
        }
        return userID;
    }

    public void signup(String username, String password) {
    // Creates a new user by adding their details to the users database
        if (checkAvailable(username)) {
            try {
                updateUserModel(generateUserID(), username, password);

                // Insert the new user details into the Users table
                String insertQuery = "INSERT INTO ACCOUNTS (USERID, USERNAME, PASSWORD) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setInt(1, model.getUserID());
                    pstmt.setString(2, model.getUsername());
                    pstmt.setString(3, model.getPassword());
                    pstmt.executeUpdate();
                    popupWindow = new PopupWindow("User successfully created!", "You are signed up. Go ahead and login!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error occurred during signup");
            }
        } else {
            popupWindow = new PopupWindow("OOPS!", "This username is already taken. Please enter a different username to signup.");
        }
    }

    public void updateUserModel(int userID, String username, String password) {
        // Sets unique userID, username and password for the new user
        model.setUserID(userID); 
        model.setUsername(username);
        model.setPassword(password);
    }
}