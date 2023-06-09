package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */
import javax.swing.SwingUtilities;

public class InventoryApp {

    public static void main(String[] args) {
        
        //calls the database manager to create tables
        DatabaseManager dB = new DatabaseManager();
        dB.establishConnection();
        dB.createAccountsTable();
        dB.createCarProductCatalogueTable();
        dB.createUserInventoryTable();
        dB.createOrderInventory();
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
