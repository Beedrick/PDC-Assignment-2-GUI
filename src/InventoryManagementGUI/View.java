package InventoryManagementGUI;

/**
 *
 * @author Gorilla Rig
 */
import javax.swing.SwingUtilities;

public class View {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginController controller = new LoginController();
                LoginGUI loginGUI = new LoginGUI(controller);

                // Set the LoginGUI as the view in the controller
                controller.setView(loginGUI);
            }
        });
    }
}
