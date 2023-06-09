package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateNewUserGUI extends JFrame {

    private CreateNewUserController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signupButton;
    private JButton cancelButton;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JPanel inputPanel;
    private GridBagConstraints gbc;

    public CreateNewUserGUI(CreateNewUserController controller) {
        this.controller = controller;
        this.inputPanel = new JPanel();
        this.titleLabel = new JLabel("SIGNUP");
        this.usernameLabel = new JLabel("Username:");
        this.passwordLabel = new JLabel("Password:");
        this.signupButton = new JButton("sign-up");
        this.cancelButton = new JButton("go back");
        this.gbc = new GridBagConstraints();
        this.usernameField = new JTextField();
        this.passwordField = new JPasswordField();

        setWindow();
        createTextField();
        createButton();
        createLabel();
        createPanel();
        addAllContents();
    }

    public void setWindow() {
        setTitle("Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void addAllContents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(40, 44, 52));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(signupButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(cancelButton);

        getContentPane().setBackground(new Color(34, 38, 46));
        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public void createTextField() {
        usernameField.setPreferredSize(new Dimension(200, 30));
        passwordField.setPreferredSize(new Dimension(200, 30));
    }

    public void createButton() {
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setBackground(new Color(95, 158, 160));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setFont(new Font("Arial", Font.BOLD, 16));
        signupButton.setPreferredSize(new Dimension(110, 40));
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get username and password from text fields
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Notify the controller about the create user attempt
                controller.signup(username, password);
                resetTextFields();
            }
        });

        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetTextFields();
                dispose(); // Dispose the CreateNewUserGUI window

                // Return to the LoginGUI window
                LoginController loginController = new LoginController();
                LoginGUI loginGUI = new LoginGUI(loginController);
                loginController.setView(loginGUI);
                loginGUI.setVisible(true);
            }
        });
    }

    public void createLabel() {
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);

        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);
    }

    public void createPanel() {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(51, 55, 64));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPanel.setPreferredSize(new Dimension(400, 120));
        inputPanel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2));
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

    public void resetTextFields() {
        // Clears text fields after user has pressed signup/goback
        usernameField.setText("");
        passwordField.setText("");
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
