package InventoryManagementGUI;

/**
 *
 * @author rocco
 */
public class LoginController {

    private LoginGUI view;

    public void setView(LoginGUI view) {
        this.view = view;
    }

    public void login(String username, String password) {
        // Perform login logic here
        // Example: check if username and password are valid
        if (username.equals("admin") && password.equals("password")) {
            System.out.println("Login successful");
            view.dispose();
            MainMenuGUI userGUI = new MainMenuGUI();
            userGUI.menuGUI();

        } else {
            // Login failed
            view.displayErrorMessage("Invalid username or password");
        }
    }
}
