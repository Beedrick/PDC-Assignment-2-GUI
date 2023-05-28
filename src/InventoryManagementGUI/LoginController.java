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
            // Login successful
            System.out.println("Login successful");
        } else {
            // Login failed
            view.displayErrorMessage("Invalid username or password");
        }
    }
}
