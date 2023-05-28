/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManagementGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Gorilla Rig
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private LoginController controller;

    public LoginGUI(LoginController controller) {
        this.controller = controller;

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(220, 220, 220));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));

        JLabel passwordLabel = new JLabel("Password:");
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

        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Notify the controller about the login attempt
                controller.login(username, password);
            }
        });

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(loginButton);

        getContentPane().setBackground(new Color(240, 240, 240));
        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
