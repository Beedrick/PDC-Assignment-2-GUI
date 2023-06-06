package InventoryManagementGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private LoginController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createUserButton;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JPanel mainPanel;
    private JPanel inputPanel;
    private GridBagConstraints gbc;
    
    public LoginGUI(LoginController controller) {
        this.controller = controller;
        this.createUserButton = new JButton("Signup");
        this.loginButton = new JButton("Login");
        this.titleLabel = new JLabel("Inventory Manager");
        this.usernameLabel = new JLabel("Username:");
        this.passwordLabel = new JLabel("Password:");
        this.mainPanel = new JPanel();
        this.inputPanel = new JPanel();
        this.usernameField = new JTextField();
        this.passwordField = new JPasswordField();
        this.gbc = new GridBagConstraints();
        
        setWindow();
        createTextField();
        createButton();
        createLabel();
        createPanel();
        addAllContents();        
    }
    
    public void setWindow() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    public void addAllContents() {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(40, 44, 52));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createUserButton);

        getContentPane().setBackground(new Color(34, 38, 46));
        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public void createPanel() {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(51, 55, 64));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
    }

    public void createLabel() {
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);

        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);

    }
    
    public void createTextField() {
        usernameField.setPreferredSize(new Dimension(200, 30));
        passwordField.setPreferredSize(new Dimension(200, 30));
    }
    public void createButton() {
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(95, 158, 160));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Notify the controller about the login attempt
                controller.login(username, password);
            }
        });

        createUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createUserButton.setBackground(new Color(52, 152, 219));
        createUserButton.setForeground(Color.WHITE);
        createUserButton.setFocusPainted(false);
        createUserButton.setFont(new Font("Arial", Font.BOLD, 16));
        createUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // sends user to signup page
//                CreateNewUserController createUserController = new CreateNewUserController();
//                CreateNewUserGUI createUserGUI = new CreateNewUserGUI(createUserController);
                dispose();
                controller.directToSignup();
//                createUserGUI.setVisible(true);
            }
        });
    }
    
    public void resetTextFields() {
    // Clears text fields after user has pressed signup/goback
        usernameField.setText("");
        passwordField.setText("");
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
