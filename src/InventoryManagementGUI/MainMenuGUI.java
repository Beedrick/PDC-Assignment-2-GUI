/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManagementGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuGUI {

    private static JPanel contentPanel; // Panel to display contents

    public static void main(String[] args) {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setSize(1850, 900); // Set the size to 1850 x 900
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(Color.lightGray);
        sidePanel.setLayout(new GridBagLayout());

        // Define common styles
        Font buttonFont = new Font("Arial", Font.BOLD, 24);
        Color buttonColor = new Color(45, 120, 230);

        // Box 1: View Inventory
        JButton viewInventoryButton = createStyledButton("View Inventory", buttonFont, buttonColor);
        viewInventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Inventory inventory = new Inventory(); // Create an instance of Inventory
                JTable inventoryTable = inventory.toTable(); // Get the inventory as a JTable
                displayContent(inventoryTable);
            }
        });

        addToSidePanel(sidePanel, viewInventoryButton, 0, 0);

        // Box 2: Exit Program
        JButton exitButton = createStyledButton("Exit", buttonFont, buttonColor);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program with a status code of 0
            }
        });

        addToSidePanel(sidePanel, exitButton, 0, 1);

        // ... (Other buttons)
        frame.add(sidePanel, BorderLayout.WEST);

        // Panel to display contents
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());
        JLabel initialLabel = new JLabel("Click a button to display content");
        initialLabel.setFont(new Font("Arial", Font.BOLD, 72));
        initialLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(initialLabel, BorderLayout.CENTER);

        frame.add(contentPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Helper method to create styled buttons
    private static JButton createStyledButton(String text, Font font, Color color) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return button;
    }

// Update the displayContent method to accept a JTable parameter
    private static void displayContent(JTable table) {
        contentPanel.removeAll(); // Clear previous content

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate(); // Refresh the panel
        contentPanel.repaint();
    }

    // Helper method to add components to the side panel with GridBagLayout
    private static void addToSidePanel(JPanel sidePanel, Component component, int gridx, int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        sidePanel.add(component, constraints);
    }

}
