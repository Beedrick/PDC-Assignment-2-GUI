package InventoryManagementGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateNewUserGUI extends JFrame {
    private CreateNewUserController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton createButton;
    private JButton cancelButton;

    public CreateNewUserGUI(CreateNewUserController controller) {
        this.controller = controller;

        setTitle("Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(40, 44, 52));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("SIGNUP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(51, 55, 64));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPanel.setPreferredSize(new Dimension(400, 120));
        inputPanel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(passwordField, gbc);

        createButton = new JButton("signup");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setBackground(new Color(95, 158, 160));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setFont(new Font("Arial", Font.BOLD, 16));
        createButton.setPreferredSize(new Dimension(110, 40));
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Notify the controller about the create user attempt
                controller.signup(username, password);
            }
        });

        cancelButton = new JButton("go back");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Dispose the CreateNewUserGUI window

                // Create and show the LoginGUI window
                LoginController loginController = new LoginController();
                LoginGUI loginGUI = new LoginGUI(loginController);
            }
        });

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(cancelButton);

        getContentPane().setBackground(new Color(34, 38, 46));
        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}




