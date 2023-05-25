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
public class LoginGUI implements ActionListener {

    private static final JFrame frame = new JFrame();
    private static final JPanel panel = new JPanel();
    private static final JTextField userText = new JTextField(20);
    private static final JLabel passLabel = new JLabel("Password");
    private static final JPasswordField passText = new JPasswordField();
    private static final JButton loginButton = new JButton("Login");
    private static final JLabel success = new JLabel("");

    public static void main(String[] args) {

        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel label = new JLabel("User");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        passLabel.setBounds(10, 50, 80, 25);
        panel.add(passLabel);

        passText.setBounds(100, 50, 165, 25);
        panel.add(passText);

        loginButton.setBounds(10, 80, 70, 25);
        loginButton.addActionListener(new LoginGUI());

        panel.add(loginButton);

        success.setBounds(10, 110, 300, 25);
        panel.add(success);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = userText.getText();
        String pass = passText.getText();

        LogIn logIn = new LogIn();

        boolean isUser = user.equals("godHuch");
        boolean isPass = pass.equals("password123");

        if (isUser && isPass) {
            success.setText("Login Successful");
            frame.dispose();

        } else {
            success.setText("Incorrect Username or Password");
        }
    }

}
