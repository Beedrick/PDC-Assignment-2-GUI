package InventoryManagementGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateOrderGUI {

    private JFrame frame;
    private JPanel contentPanel;
    private Font buttonFont;
    private Color buttonColor;
    private String currentUser;

    public CreateOrderGUI(String userName) {
        //userName is passed through when called from MainMenuGUI so when goBack button is clicked 
        //the current users username can be used to create a new MainMenuGUI under the same user 
        //otherwise userID is set to 0 by default because the method to get userID is using a null String
        this.currentUser = userName;
        frame = new JFrame("Create Order");
        frame.setSize(1400, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.buttonFont = new Font("Arial", Font.BOLD, 24);
        this.buttonColor = new Color(45, 120, 230);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Create the left panel with the desired color
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(40, 44, 52));
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.setLayout(new GridLayout(3, 1, 0, 10)); // Set grid layout with 3 rows, 1 column, and spacing

        // Create buttons and add them to the left panel
        JButton confirmButton = createStyledButton("Confirm Order", buttonFont, buttonColor);
        JButton orderHistoryButton = createStyledButton("Order History", buttonFont, buttonColor);
        JButton goBackButton = createStyledButton("Back to Main Menu", buttonFont, buttonColor);

        // Add action listeners to the buttons
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle confirm button click
            }
        });

        orderHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle order history button click
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainMenuController backMainControl = new MainMenuController();
                backMainControl.setCurrentUser(userName);
                MainMenuGUI backMain = new MainMenuGUI(backMainControl);

            }
        });

        // Add buttons to the left panel
        leftPanel.add(confirmButton);
        leftPanel.add(orderHistoryButton);
        leftPanel.add(goBackButton);

        contentPanel.add(leftPanel, BorderLayout.WEST);

        // Customize the content panel as needed
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        frame.add(contentPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JButton createStyledButton(String text, Font font, Color color) {
        // Helper method to create styled buttons
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return button;
    }

    public void disposeFrame() {
        frame.dispose();
    }
    
}
