package InventoryManagementGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class CreateOrderGUI {

    private JFrame frame;
    private JPanel contentPanel;
    private Font buttonFont;
    private Color buttonColor;
    public MainMenuController currUser;
    private MainMenuGUI mMG;
    private JTable table;
    private JButton downloadReceiptButton;
    private CreateOrderController controller;
    private static int orderID = 1;

    public CreateOrderGUI(MainMenuController currUser) {
        this.controller = controller;
        this.currUser = currUser;
        frame = new JFrame("Create Order");
        frame.setSize(1400, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.buttonFont = new Font("Arial", Font.BOLD, 24);
        this.buttonColor = new Color(45, 120, 230);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

// Create a header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(40, 44, 52));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

// Create a label for the header text
        JLabel headerLabel = new JLabel("Your Order");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

// Add the header panel to the top of the content panel
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create the left panel with the desired color
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(40, 44, 52));
        leftPanel.setLayout(new GridBagLayout());// Use GridBagLayout for flexible layout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 0, 10); // Adjust spacing between buttons

        // Create buttons and add them to the left panel
        JButton confirmButton = createStyledButton("Confirm Order", buttonFont, buttonColor);
        JButton orderHistoryButton = createStyledButton("Order History", buttonFont, buttonColor);
        JButton goBackButton = createStyledButton("Back to Main Menu", buttonFont, buttonColor);
        JButton exitButton = createStyledButton("Exit", buttonFont, buttonColor);
        downloadReceiptButton = createStyledButton("Download Receipt", buttonFont, buttonColor);
        downloadReceiptButton.setVisible(false); // Set downloadReceiptButton initially invisible

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

        downloadReceiptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                downloadReceipt();
            }
        });

        orderHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Fetch order history data from the database
                DefaultTableModel orderHistoryModel = fetchOrderHistoryFromDatabase();

                // Create a new frame for displaying the order history
                JFrame orderHistoryFrame = new JFrame("Order History");
                orderHistoryFrame.setSize(800, 600);
                orderHistoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Create a table using the order history table model
                JTable orderHistoryTable = new JTable(orderHistoryModel);
                JScrollPane scrollPane = new JScrollPane(orderHistoryTable);

                // Add the table to the frame
                orderHistoryFrame.add(scrollPane);

                // Set the frame visible
                orderHistoryFrame.setLocationRelativeTo(null);
                orderHistoryFrame.setVisible(true);
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to commit your order?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Perform order confirmation actions here

                    // Insert data from the table into the ORDERINVENTORY table
                    insertDataIntoOrderInventory();

                    // Remove user's inventory from the INVENTORY table
                    removeUserInventory();

                    // Increment order ID for the next order
                    orderID++;

                    // Set downloadReceiptButton visible after order confirmation
                    downloadReceiptButton.setVisible(true);
                }
            }
        });

        // Add the Confirm Order button
        leftPanel.add(confirmButton, gbc);

        // Add the Order History button
        leftPanel.add(orderHistoryButton, gbc);

// Create a new panel for the button and set its layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

// Add the Download Receipt button to the button panel
        buttonPanel.add(downloadReceiptButton);

// Add the button panel to the content panel at the bottom
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        gbc.insets = new Insets(10, 10, 10, 10); // Adjust spacing between buttons
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.weighty = 1.0;

        // Add the Back to Main Menu button
        leftPanel.add(goBackButton, gbc);

        // Add the Download Receipt button
        // Add the Exit button
        leftPanel.add(exitButton, gbc);

        contentPanel.add(leftPanel, BorderLayout.WEST);

        // Fetch data from the database and create the table
        DefaultTableModel tableModel = fetchInventoryDataFromDatabase();

        // Create the table using the table model
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        // Create a panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

// Add the table panel to the content panel
        contentPanel.add(tablePanel, BorderLayout.CENTER);
    }

    public void setVisible() {
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

        // Calculate the preferred width based on the button's text
        FontMetrics fontMetrics = button.getFontMetrics(button.getFont());
        int textWidth = fontMetrics.stringWidth(text);
        int preferredWidth = textWidth + 20;  // Adjust padding as needed

        button.setPreferredSize(new Dimension(preferredWidth, 40)); // Adjust the button size here
        button.setMaximumSize(button.getPreferredSize());
        button.setMinimumSize(button.getPreferredSize());
        button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Adjust padding

        return button;
    }

    public DefaultTableModel fetchOrderHistoryFromDatabase() {
        // JDBC database connection details
        String url = "jdbc:derby:InventoryDB1;";
        String username = "gui";
        String password = "gui";

        // Create a table model with column names
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ORDERID", "USERID", "PRODUCTNAME", "PRODUCTBRAND", "PRODUCTPRICE", "PRODUCTTYPE", "PRODUCTQUANTITY"});

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL statement with a parameter
            String query = "SELECT * FROM ORDERINVENTORY WHERE USERID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, currUser.getUserID(currUser.getCurrentUser())); // Set the parameter value

            // Execute the query and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                int orderID = resultSet.getInt("ORDERID");
                int userID = resultSet.getInt("USERID");
                String prodName = resultSet.getString("PRODUCTNAME");
                String prodBrand = resultSet.getString("PRODUCTBRAND");
                double price = resultSet.getDouble("PRODUCTPRICE");
                String prodType = resultSet.getString("PRODUCTTYPE");
                int quantity = resultSet.getInt("PRODUCTQUANTITY");

                model.addRow(new Object[]{orderID, userID, prodName, prodBrand, price, prodType, quantity});
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public DefaultTableModel fetchInventoryDataFromDatabase() {
        // JDBC database connection details
        String url = "jdbc:derby:InventoryDB1;";
        String username = "gui";
        String password = "gui";

        // Create a table model with column names
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"USERID", "PRODUCTNAME", "PRODUCTBRAND", "PRODUCTPRICE", "PRODUCTTYPE", "PRODUCTQUANTITY"});

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL statement with a parameter
            String query = "SELECT * FROM INVENTORY WHERE USERID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, currUser.getUserID(currUser.getCurrentUser())); // Set the parameter value

            // Execute the query and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                int id = resultSet.getInt("USERID");
                String prodName = resultSet.getString("PRODUCTNAME");
                String prodBrand = resultSet.getString("PRODUCTBRAND");
                double price = resultSet.getDouble("PRODUCTPRICE");
                String prodType = resultSet.getString("PRODUCTTYPE");
                int quantity = resultSet.getInt("PRODUCTQUANTITY");

                model.addRow(new Object[]{id, prodName, prodBrand, price, prodType, quantity});
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public void downloadReceipt() {
        try {
            String filename = "receipt.txt";
            FileWriter writer = new FileWriter(filename);
            writer.write("=================================\n");
            writer.write("           Receipt\n");
            writer.write("=================================\n");
            writer.write("User ID: " + currUser.getUserID(currUser.getCurrentUser()) + "\n");

            int[] columnWidths = {15, 15, 10, 10}; // Widths for Product, Brand, Price, and Quantity columns

            // Create the header line with flexible border formatting
            writer.write(createHeaderLine(columnWidths));

            double total = 0;

            for (int i = 0; i < table.getRowCount(); i++) {
                String productName = (String) table.getValueAt(i, 1);
                String brand = (String) table.getValueAt(i, 2);
                double price = (double) table.getValueAt(i, 3);
                int quantity = (int) table.getValueAt(i, 5);

                writer.write(formatField(productName, columnWidths[0]) + "\t");
                writer.write(formatField(brand, columnWidths[1]) + "\t");
                writer.write(formatField(String.format("$%.2f", price), columnWidths[2]) + "\t");
                writer.write(formatField(Integer.toString(quantity), columnWidths[3]) + "\n");

                total += price * quantity;
            }

            // Create the footer line with flexible border formatting
            writer.write(createFooterLine(columnWidths));

            writer.write(String.format("Total: $%.2f\n", total));
            writer.close();
            JOptionPane.showMessageDialog(frame, "Receipt downloaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to download receipt.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String createHeaderLine(int[] columnWidths) {
        StringBuilder line = new StringBuilder();
        line.append("-------------------------------------------------\n");
        line.append("Product\t\tBrand\t\tPrice\t Quantity\n");
        line.append("-------------------------------------------------\n");
        return line.toString();
    }

    private String createFooterLine(int[] columnWidths) {
        return "-------------------------------------------------\n";
    }

    private String formatField(String field, int width) {
        int fieldWidth = field.length();
        if (fieldWidth > width) {
            return field.substring(0, width);
        } else {
            int spaces = width - fieldWidth;
            StringBuilder paddedField = new StringBuilder(field);
            for (int i = 0; i < spaces; i++) {
                paddedField.append(" ");
            }
            return paddedField.toString();
        }
    }

    private void insertDataIntoOrderInventory() {
        // JDBC database connection details
        String url = "jdbc:derby:InventoryDB1;";
        String username = "gui";
        String password = "gui";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Prepare the SQL statement to insert data into the ORDERINVENTORY table
            String insertQuery = "INSERT INTO ORDERINVENTORY (ORDERID, USERID, PRODUCTNAME, PRODUCTBRAND, PRODUCTPRICE, PRODUCTTYPE, PRODUCTQUANTITY) "
                    + "SELECT ?, USERID, PRODUCTNAME, PRODUCTBRAND, PRODUCTPRICE, PRODUCTTYPE, PRODUCTQUANTITY FROM INVENTORY WHERE USERID = ?";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setInt(1, getOrderID()); // Set the order ID
            statement.setInt(2, currUser.getUserID(currUser.getCurrentUser())); // Set the user ID

            // Execute the insert statement
            statement.executeUpdate();

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getOrderID() {
        int orderID = 1;

        // JDBC database connection details
        String url = "jdbc:derby:InventoryDB1;";
        String username = "gui";
        String password = "gui";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create a statement to execute the query
            Statement statement = connection.createStatement();

            // Execute the query to retrieve the maximum order ID
            ResultSet resultSet = statement.executeQuery("SELECT MAX(ORDERID) FROM ORDERINVENTORY");

            // Check if the result set has a value
            if (resultSet.next()) {
                orderID = resultSet.getInt(1) + 1; // Increment the maximum order ID by 1
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderID;
    }

    private void removeUserInventory() {
        // JDBC database connection details
        String url = "jdbc:derby:InventoryDB1;";
        String username = "gui";
        String password = "gui";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Prepare the SQL statement to delete rows from the INVENTORY table
            String deleteQuery = "DELETE FROM INVENTORY WHERE USERID = ?";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, currUser.getUserID(currUser.getCurrentUser())); // Set the user ID

            // Execute the delete statement
            statement.executeUpdate();

            // Close the statement
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
