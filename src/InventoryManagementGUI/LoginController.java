package InventoryManagementGUI;

/**
 *
 * @author rocco
 */
public class LoginController {

    private LoginGUI view;

    public LoginController() {

    }

    public void setView(LoginGUI view) {
        this.view = view;
    }

    public void login(String username, String password) {
        // Perform login logic here
        // Example: check if username and password are valid
        if (username.equals("admin") && password.equals("password")) {
            System.out.println("Login successful");

            MainMenuGUI userGUI = new MainMenuGUI();
            userGUI.menuGUI();

            view.dispose();

        } else {
            // Login failed
            view.displayErrorMessage("Invalid username or password");
        }
    }
}
