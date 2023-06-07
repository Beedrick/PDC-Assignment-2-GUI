package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */

import java.sql.*;

public class LoginController {
    private LoginGUI view;
    private LogIn model;
    private MainMenuController menuController;
    private PopupWindow popupWindow;
    public static Connection conn;  

    public LoginController() {
        DatabaseManager dbManager = new DatabaseManager();
        this.conn = dbManager.getConnection();
        this.model = new LogIn();
        this.menuController = new MainMenuController();
    }

    public void setView(LoginGUI view) {
        this.view = view;
    }

    public boolean validateLogin(String username, String password) {
    // Checks if the username-password combo is valid
        boolean valid = false;
        String query = "SELECT * FROM Accounts WHERE LOWER(username) = LOWER(?) AND password = ?";
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try { 
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();
            valid = rs.next();

            return valid;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return valid;
    } 

    public boolean login(String username, String password) {
        // Checks for valid username-password match and then allows the user to login
        boolean closeWindow = false;

        if (validateLogin(username, password)) {
            updateCurrentUser(username);
            directToMainMenu();
            closeWindow = true;
        } else {
            view.resetTextFields();
            popupWindow = new PopupWindow("Invalid login", "Your username or password is incorrect. \nPlease try again.");
        }

        return closeWindow;
    }
    
    public void updateCurrentUser(String username) {
    // Sets logged in user details
        model.setUsername(username);
        menuController.setCurrentUser(username); // gives MainMenu the current username
    }

    public void directToSignup() {
    // Sends user to signup page
        CreateNewUserController createUserController = new CreateNewUserController();
        CreateNewUserGUI createUserGUI = new CreateNewUserGUI(createUserController);
    }
    
    public void directToMainMenu() {
    // Sends user to main menu
        MainMenuGUI menuGUI = new MainMenuGUI(menuController);
    }
}