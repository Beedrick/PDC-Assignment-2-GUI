package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PopupWindow {

    public PopupWindow() {

    }

    public void displayPopup(String title, String message) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
