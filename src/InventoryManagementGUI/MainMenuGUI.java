/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManagementGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MainMenuGUI {

    private static JPanel contentPanel; // Panel to display contents
    private DatabaseManager databaseManager;

    public void menuGUI() {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setSize(1850, 900); // Set the size to 1850 x 900
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(40, 44, 52));
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

        // Box 3: Additional Button 1
        JButton additionalButton1 = createStyledButton("Car Products Catalogue", buttonFont, buttonColor);
        additionalButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                databaseManager = new DatabaseManager();

                String sql = "SELECT * FROM CARPRODUCTCATALOGUE"; // SQL query to select all rows from the table
                ResultSet resultSet = databaseManager.queryDB(sql); // Execute the query and get the result set

                // Create a table model to hold the data from the result set
                DefaultTableModel tableModel = new DefaultTableModel();
                try {
                    // Get the column names from the result set metadata
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        tableModel.addColumn(metaData.getColumnLabel(i));
                    }

                    // Add the rows to the table model
                    while (resultSet.next()) {
                        Object[] rowData = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            rowData[i - 1] = resultSet.getObject(i);
                        }
                        tableModel.addRow(rowData);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                JTable carCatalogueTable = new JTable(tableModel); // Create a table using the table model
                customizeTable(carCatalogueTable);
                displayContent(carCatalogueTable); // Display the table in full screen
            }
        });

        addToSidePanel(sidePanel, additionalButton1, 0, 2);

        // Box 4: Additional Button 2
        JButton additionalButton2 = createStyledButton("Button 2", buttonFont, buttonColor);
        additionalButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action for Button 2 to be set
            }
        });

        addToSidePanel(sidePanel, additionalButton2, 0, 3);

        // Box 5: Additional Button 3
        JButton additionalButton3 = createStyledButton("Button 3", buttonFont, buttonColor);
        additionalButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action for Button 3 to be set
            }
        });

        addToSidePanel(sidePanel, additionalButton3, 0, 4);

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

        // Create a scrollable pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Set the layout manager of the contentPanel to BorderLayout
        contentPanel.setLayout(new BorderLayout());

        // Set the preferred size of the scrollPane to match the contentPanel's size
        scrollPane.setPreferredSize(contentPanel.getSize());

        // Set the scroll pane as the content of the panel
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

    // Helper method to customize the appearance and behavior of the table
    private static void customizeTable(JTable table) {
        // Apply custom cell renderers
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        // Apply custom header renderer
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new CustomTableHeaderRenderer());

        // Disable auto resizing of columns
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    // Custom cell renderer to apply desired styles to the table cells
    private static class CustomTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Apply desired styles to the cell component
            // Example: Set a custom background color for even rows
            if (row % 2 == 0) {
                component.setBackground(new Color(240, 240, 240));
            } else {
                component.setBackground(Color.WHITE);
            }

            return component;
        }
    }

    // Custom header renderer to apply desired styles to the table header
    private static class CustomTableHeaderRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Apply desired styles to the header component
            // Example: Set a custom background color and font
            component.setBackground(new Color(45, 120, 230));
            component.setForeground(Color.WHITE);
            component.setFont(new Font("Arial", Font.BOLD, 14));

            return component;
        }
    }
}
