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
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.util.Arrays;

public class MainMenuGUI {

    private MainMenuController controller;
    private Font buttonFont;
    private Color buttonColor;
    private JFrame frame;
    private JPanel sidePanel;
    private JButton viewInventoryButton;
    private JButton exitButton;
    private JButton viewCarProductsButton;
    private JButton addProductButton;
    private JButton createOrderButton;
    private JButton removeProductButton;
    private JButton updateQuantityButton;
    private JTextField productIDTextField;
    private JTextField quantityTextField;
    private JButton updateAddButton;
    private JButton updateRemoveButton;
    private JButton changeQuantityButton;
    private GridBagConstraints constraints;
    private static JPanel contentPanel; // Panel to display contents
    private DatabaseManager databaseManager;
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private Inventory[] inventoryArray;

    public MainMenuGUI(MainMenuController controller) {
        this.controller = controller;
        this.buttonFont = new Font("Arial", Font.BOLD, 24);
        this.buttonColor = new Color(45, 120, 230);
        this.frame = new JFrame("Inventory Management System");
        this.sidePanel = new JPanel();
        this.constraints = new GridBagConstraints();
        this.viewInventoryButton = createStyledButton("View Inventory", buttonFont, buttonColor);
        this.viewCarProductsButton = createStyledButton("Car Products Catalogue", buttonFont, buttonColor);
        this.addProductButton = createStyledButton("Add Product", buttonFont, buttonColor);
        this.removeProductButton = createStyledButton("Remove Product", buttonFont, buttonColor);
        this.updateQuantityButton = createStyledButton("Update Quantity", buttonFont, buttonColor);
        this.exitButton = createStyledButton("Exit", buttonFont, buttonColor);
        this.productIDTextField = new JTextField();
        this.quantityTextField = new JTextField();
        this.updateAddButton = new JButton("");
        this.updateRemoveButton = new JButton("");
        this.changeQuantityButton = new JButton("");

        createOrderButton = createStyledButton("Create Order", buttonFont, buttonColor);
        createOrderButton.setVisible(false);

        createTable();
        setFrame();
        setSidePanel();
        setButtons();
        displayFrame();
    }

    public void createTable() {
        this.inventoryTableModel = new DefaultTableModel();
        this.inventoryTableModel.setColumnIdentifiers(new String[]{"Product Name", "Product Brand", "Product Price", "Product Type", "Product Quantity"});
    }

    public JButton createStyledButton(String text, Font font, Color color) {
        // Helper method to create styled buttons
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return button;
    }

    public void addToSidePanel(JPanel sidePanel, Component component, int gridx, int gridy) {
        // Helper method to add components to the side panel with GridBagLayout
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        sidePanel.add(component, constraints);
    }

    public void displayUpdateInventory(int updateType) {
        /* Updates the panel to display content for user to update their inventory
        UPDATE TYPES:
            1) Add a product
            2) Remove a product
            3) Update quantity of product */

        String[] updateStrings = controller.getUpdateStrings(updateType);

        contentPanel.removeAll(); // Clear previous content

        // Set GridBagLayout as the layout manager
        contentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components

        JLabel titleLabel = new JLabel(updateStrings[0]);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Update font and size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center-align the title
        contentPanel.add(titleLabel, gbc);

        // Create labels for ProductID and Add
        JLabel productIDLabel = new JLabel("Product ID: ");
        productIDLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Update font and size
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST; // Right-align the label
        contentPanel.add(productIDLabel, gbc);

        productIDTextField.setPreferredSize(new Dimension(250, 40));
        productIDTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST; // Left-align the text field
        contentPanel.add(productIDTextField, gbc);

        if (updateType == 1 || updateType == 3) {
            JLabel quantityLabel = new JLabel(updateStrings[1]);
            quantityLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Update font and size
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST; // Right-align the label
            contentPanel.add(quantityLabel, gbc);

            quantityTextField.setPreferredSize(new Dimension(250, 40));
            quantityTextField.setFont(new Font("Arial", Font.PLAIN, 18));
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST; // Left-align the text field
            contentPanel.add(quantityTextField, gbc);
        }

        // Update confirm button based on updateType
        if (updateType == 1) { // ADD PRODUCT
            updateAddButton.setVisible(true);
            updateRemoveButton.setVisible(false);
            changeQuantityButton.setVisible(true);

            updateAddButton.setText(updateStrings[2]);
            updateAddButton.setFont(new Font("Arial", Font.BOLD, 18)); // Update font and size
            updateAddButton.setBackground(new Color(0, 150, 0)); // Darker green color

            updateAddButton.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER; // Center-align the button
            gbc.ipady = 20; // Increase button height
            contentPanel.add(updateAddButton, gbc);
            updateAddButton.setBorder(null);

        } else if (updateType == 2) { // REMOVE PRODUCT
            updateAddButton.setVisible(false);
            updateRemoveButton.setVisible(true);
            changeQuantityButton.setVisible(true);

            updateRemoveButton.setText(updateStrings[2]);
            updateRemoveButton.setFont(new Font("Arial", Font.BOLD, 18)); // Update font and size
            updateRemoveButton.setBackground(new Color(0, 150, 0)); // Darker green color

            updateRemoveButton.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER; // Center-align the button
            gbc.ipady = 20; // Increase button height
            contentPanel.add(updateRemoveButton, gbc);
            updateRemoveButton.setBorder(null);

        } else if (updateType == 3) { // UPDATE QUANTITY
            updateAddButton.setVisible(false);
            updateRemoveButton.setVisible(false);
            changeQuantityButton.setVisible(true);

            changeQuantityButton.setText(updateStrings[2]);
            changeQuantityButton.setFont(new Font("Arial", Font.BOLD, 18)); // Update font and size
            changeQuantityButton.setBackground(new Color(0, 150, 0)); // Darker green color

            changeQuantityButton.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER; // Center-align the button
            gbc.ipady = 20; // Increase button height
            contentPanel.add(changeQuantityButton, gbc);
            changeQuantityButton.setBorder(null);
        }

        // Repaint the content panel to reflect the changes
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void displayTable(JTable table) {

        //Displays the create order button if and only if there is a table of inventory to display
        if (table != null) {
            createOrderButton.setVisible(true);
        } else {
            createOrderButton.setVisible(false);
        }

        // Update the displayTable method to accept a JTable parameter
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

    public void setFrame() {
        frame.setSize(1400, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(sidePanel, BorderLayout.WEST);
    }

    public void setSidePanel() {
        sidePanel.setBackground(new Color(40, 44, 52));
        sidePanel.setLayout(new GridBagLayout());

        // Add side panel contents
        addToSidePanel(sidePanel, viewInventoryButton, 0, 0);
        addToSidePanel(sidePanel, addProductButton, 0, 1);
        addToSidePanel(sidePanel, removeProductButton, 0, 2);
        addToSidePanel(sidePanel, updateQuantityButton, 0, 3);
        addToSidePanel(sidePanel, viewCarProductsButton, 0, 4);
        addToSidePanel(sidePanel, exitButton, 0, 5);
    }

    public void setButtons() {
        // Box 1: View Inventory
        viewInventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inventoryArray = controller.getInventory(); // get current users inventory from DB as an array
                inventoryTableModel.setRowCount(0); // clear inventory table

                // loop through currentUser inventory array
                for (Inventory item : inventoryArray) {
                    Object[] rowData = new Object[]{
                        item.getProductName(),
                        item.getProductBrand(),
                        item.getProductPrice(),
                        item.getProductType(),
                        item.getProductQuantity()
                    };
                    inventoryTableModel.addRow(rowData);
                }

                inventoryTable = new JTable(inventoryTableModel);
                displayTable(inventoryTable);

                addOrderButtonToPanel(); // allows user to create an order
                createOrderButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Dispose the current frame
                        frame.dispose();
                        // Create a new CreateOrderGUI window
                        CreateOrderGUI createOrderGUI = new CreateOrderGUI(controller);
                    }
                });
            }
        });

        // Box 2: Add a product button
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayUpdateInventory(1);

                // confirmButton method for adding product
                updateAddButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // get values from text fields
                        String productID = productIDTextField.getText();
                        String quantityText = quantityTextField.getText();
                        int quantity = Integer.parseInt(quantityText);

                        controller.addProduct(productID, quantity); // add product to DB
                        resetTextFields();
                    }
                });
            }
        });
        
        // Box 3: Remove a product button
        removeProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayUpdateInventory(2);

                // confirmButton method for removing product
                updateRemoveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // get values from text fields
                        String productID = productIDTextField.getText();
                        controller.removeProduct(productID); // remove product from DB
                        resetTextFields();
                    }
                });
            }
        });

        // Box 4: Update quantity of a product button
        updateQuantityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayUpdateInventory(3);

                // confirmButton method for adding product
                changeQuantityButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // get values from text fields
                        String productID = productIDTextField.getText();
                        String quantityText = quantityTextField.getText();
                        int quantity = Integer.parseInt(quantityText);

                        controller.updateProductQuantity(productID, quantity); // update quantity of a product
                        resetTextFields();
                    }
                });
            }
        });

        // Box 5: viewCarProducts Button
        viewCarProductsButton.addActionListener(new ActionListener() {
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
                //customizeTable(carCatalogueTable);
                displayTable(carCatalogueTable); // Display the table in full screen
            }
        });

        // Box 6: Exit Program
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program with a status code of 0
            }
        });
    }

    private void addOrderButtonToPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new BorderLayout());

        // Create a panel for vertical spacing
        JPanel spacerPanel = new JPanel();
        spacerPanel.setBackground(Color.WHITE);
        spacerPanel.setPreferredSize(new Dimension(10, 40));
        buttonPanel.add(spacerPanel, BorderLayout.NORTH); // Add the spacer panel to the top

        JPanel orderButtonPanel = new JPanel();
        orderButtonPanel.setBackground(Color.WHITE);
        orderButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Add the order button to the orderButtonPanel
        orderButtonPanel.add(createOrderButton);

        // Add the orderButtonPanel to the buttonPanel
        buttonPanel.add(orderButtonPanel, BorderLayout.CENTER);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void displayFrame() {
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

    public void resetTextFields() {
        productIDTextField.setText("");
        quantityTextField.setText("");
    }
}
