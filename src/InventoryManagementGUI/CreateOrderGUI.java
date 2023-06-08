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
    public MainMenuController currUser;

    public CreateOrderGUI(MainMenuController currUser) {
        this.currUser = currUser;
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
        leftPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for flexible layout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 0, 10); // Adjust spacing between buttons

        // Create buttons and add them to the left panel
        JButton confirmButton = createStyledButton("Confirm Order", buttonFont, buttonColor);
        JButton orderHistoryButton = createStyledButton("Order History", buttonFont, buttonColor);
        JButton goBackButton = createStyledButton("Back to Main Menu", buttonFont, buttonColor);
        JButton exitButton = createStyledButton("Exit", buttonFont, buttonColor);

        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainMenuGUI backMain = new MainMenuGUI(currUser);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program with a status code of 0
            }
        });

        // Add the Confirm Order button
        leftPanel.add(confirmButton, gbc);
        // Add the Order History button
        leftPanel.add(orderHistoryButton, gbc);

        gbc.insets = new Insets(10, 10, 10, 10); // Adjust spacing between buttons
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.weighty = 1.0;

        // Add the Back to Main Menu button
        leftPanel.add(goBackButton, gbc);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridwidth = GridBagConstraints.REMAINDER;
        gbc2.insets = new Insets(10, 10, 0, 10); // Adjust spacing between buttons

        // Add the Exit button
        leftPanel.add(exitButton, gbc2);

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
        button.setPreferredSize(new Dimension(200, 40)); // Adjust the button size here
        button.setMaximumSize(button.getPreferredSize());
        button.setMinimumSize(button.getPreferredSize());
        button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Adjust padding
        return button;
    }

    public void disposeFrame() {
        frame.dispose();
    }
}
