package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */
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
    private CreateOrderController controller;
    private JButton confirmButton;
    private JButton orderHistoryButton;
    private JButton goBackButton;
    private JButton exitButton;
    // inventory table
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private CreateOrder[] inventoryArray;
    // car catalogue table
    private static int orderID = 1;
    private CreateOrder[] storedInven;

    public CreateOrderGUI(MainMenuController currUser) {

        this.controller = new CreateOrderController();
        this.currUser = currUser;
        frame = new JFrame("Create Order");
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        this.controller.setCurrentUser(currUser.getCurrentUser());
        storedInven = controller.getInventory();

        createTables();
        setOrderButtonStyle();
        createOrderButtons();
        setOrderButtons();
        setPanel();
        setFrame();

    }

    //Setting frame
    public void setFrame() {

        frame.setSize(1400, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(contentPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void setPanel() {
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
        // Add the Confirm Order button
        leftPanel.add(confirmButton, gbc);
        // Add the Order History button
        leftPanel.add(orderHistoryButton, gbc);
// Create a new panel for the button and set its layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
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
        //displayTable(fetchInventoryDataFromDatabase());
        // Create the table using the table model
        // table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        // Create a panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        // Add the table panel to the content panel
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        displayTable(openInventoryPanel());

    }

    //Setting action listeners for Confirming order button
    public void confirmButton() {
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to commit your order?\nYour Inventory will be reset if yes.", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Perform order confirmation actions here

                    // Insert data from the table into the ORDERINVENTORY table
                    insertDataIntoOrderInventory();

                    storedInven = controller.getInventory();

                    // Remove user's inventory from the INVENTORY table
                    controller.removeUserInventory(currUser);

                    // Increment order ID for the next order
                    orderID++;

                }
            }
        });
    }

    //Setting action listeners for Order History Button
    public void historyButton() {
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
    }

    //Initialize JButtons for this class using method createStyledButton()
    public void createOrderButtons() {
        this.confirmButton = createStyledButton("Confirm Order", buttonFont, buttonColor);
        this.orderHistoryButton = createStyledButton("Order History", buttonFont, buttonColor);
        this.goBackButton = createStyledButton("Back to Main Menu", buttonFont, buttonColor);
        this.exitButton = createStyledButton("Exit", buttonFont, buttonColor);
    }

    //sets formatting and color for button style
    public void setOrderButtonStyle() {

        this.buttonFont = new Font("Arial", Font.BOLD, 24);
        this.buttonColor = new Color(45, 120, 230);
    }

    //method that sets all JButtons action listeners
    public void setOrderButtons() {
        goBack();
        exitButton();
        historyButton();
        confirmButton();
    }

    public void goBack() {
        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainMenuGUI backMain = new MainMenuGUI(currUser);
            }
        });

    }

    public void exitButton() {

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program with a status code of 0
            }
        });

    }

    public void disposeFrame() {
        frame.dispose();
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

    public JTable openInventoryPanel() {

        inventoryArray = controller.getInventory(); // get current users inventory from DB as an inventory obj array
        inventoryTableModel.setRowCount(0); // clear inventory table

        // loop through currentUser inventory array
        for (CreateOrder item : inventoryArray) {
            Object[] rowData = new Object[]{
                item.getProductName(),
                item.getProductBrand(),
                item.getProductPrice(),
                item.getProductType(),
                item.getProductQuantity()
            };
            inventoryTableModel.addRow(rowData); // add current obj to corresponding row
        }

        // turn table model into table and display users current inventory
        inventoryTable = new JTable(inventoryTableModel);

        return inventoryTable;

    }

    public JTable recieptArray() {

        inventoryTableModel.setRowCount(0); // clear inventory table

        // loop through currentUser inventory array
        for (CreateOrder item : this.storedInven) {
            Object[] rowData = new Object[]{
                item.getProductName(),
                item.getProductBrand(),
                item.getProductPrice(),
                item.getProductType(),
                item.getProductQuantity()
            };
            inventoryTableModel.addRow(rowData); // add current obj to corresponding row
        }

        // turn table model into table and display users current inventory
        return new JTable(inventoryTableModel);

    }

    public void displayTable(JTable table) {

        // Create a scrollable pane for the table
        JScrollPane scrollPane = new JScrollPane(table);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public void downloadReceipt() {
        try {

            JTable recieptTable = recieptArray();

            String filename = "receipt.txt";
            FileWriter writer = new FileWriter(filename);
            writer.write("=================================\n");
            writer.write("           Receipt\n");
            writer.write("=================================\n");
            writer.write("User ID: " + currUser.getUserID(currUser.getCurrentUser()) + "\n");

            int[] columnWidths = {15, 15, 10, 10, 10};

            // Create the header line with flexible border formatting
            writer.write(createHeaderLine(columnWidths));

            double total = 0;

            for (int i = 0; i < recieptTable.getRowCount(); i++) {
                String productName = (String) recieptTable.getValueAt(i, 1);
                String brand = (String) recieptTable.getValueAt(i, 2);
                double price = (double) recieptTable.getValueAt(i, 3);
                String prodType = (String) recieptTable.getValueAt(i, 4);
                int quantity = (int) recieptTable.getValueAt(i, 5);

                writer.write(formatField(productName, columnWidths[0]) + "\t");
                writer.write(formatField(brand, columnWidths[1]) + "\t");
                writer.write(formatField(String.format("$%.2f", price), columnWidths[2]) + "\t");
                writer.write(formatField(prodType, columnWidths[3]) + "\t");
                writer.write(formatField(Integer.toString(quantity), columnWidths[4]) + "\n");

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

    public void createTables() {
        // Create inventory and car catalogue table model
        this.inventoryTableModel = new DefaultTableModel();
        this.inventoryTableModel.setColumnIdentifiers(new String[]{"Product Name", "Product Brand", "Product Price", "Product Type", "Product Quantity"});
    }

}
