package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */

import java.sql.*;

public class LoginController {
    private LoginGUI view;
    private PopupWindow popupWindow;
    private LogIn model;
    public static Connection conn;  

    public LoginController() {
        DatabaseManager dbManager = new DatabaseManager();
        this.conn = dbManager.getConnection();
        this.model = new LogIn();
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

    public void login(String username, String password) {
        // Checks for valid username-password match and then allows the user to login
        if (validateLogin(username, password)) {
            directToMainMenu();
        } else {
            view.resetTextFields();
            popupWindow = new PopupWindow("Invalid login", "Your username or password is incorrect. \nPlease try again.");
        }
    }

    public void directToSignup() {
        CreateNewUserController createUserController = new CreateNewUserController();
        CreateNewUserGUI createUserGUI = new CreateNewUserGUI(createUserController);
        createUserGUI.setVisible(true);
    }
    
    public void directToMainMenu() {
        MainMenuGUI userGUI = new MainMenuGUI();
        userGUI.menuGUI();
    }
}
