package InventoryManagementGUI;

/**
 *
 * @author Gorilla Rig
 */
import javax.swing.SwingUtilities;

public class View {

    public static void main(String[] args) {
        
        //calls the database manager to create tables
        DatabaseManager dB = new DatabaseManager();
        dB.establishConnection();
        dB.createAccountsTable();
        dB.createCarProductCatalogueTable();
        dB.createUserInventoryTable();
        
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
