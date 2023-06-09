package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;

public class MainMenuGUI {

    private PopupWindow popupWindow;
    private MainMenuController controller;
    // styling for side panel buttons
    private Font buttonFont;
    private Color buttonColor;
    // menu buttons that belong in side panel
    private JButton viewInventoryButton;
    private JButton viewCarProductsButton;
    private JButton addProductMenuButton;
    private JButton createOrderButton;
    private JButton removeProductMenuButton;
    private JButton updateQuantityMenuButton;
    private JButton exitButton;
    // text fields for manipulating inventory
    private JTextField productIDTextField;
    private JTextField quantityTextField;
    // buttons that update the inventory
    private JButton addProductButton;
    private JButton removeProductButton;
    private JButton updateQuantityButton;
    // inventory table
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private Inventory[] inventoryArray;
    private DatabaseManager databaseManager;
    // car catalogue table
    private JTable carCatalogueTable;
    private DefaultTableModel carCatalogueTableModel;
    private CarCatalogue[] carCatalogueArray;
    // panels and frames to display content
    private GridBagConstraints constraints;
    private JFrame frame;
    private JPanel sidePanel;
    private static JPanel contentPanel;
    private boolean carCatalogueDisplayed = false;

    public MainMenuGUI(MainMenuController controller) {
        this.controller = controller;
        // panels and frames to display content
        this.frame = new JFrame("Inventory Management System");
        this.sidePanel = new JPanel();
        this.constraints = new GridBagConstraints();

        // Methods which intalise components of the GUI
        createTables();
        setMenuButtonStyle();
        createMenuButtons();
        createUpdateInventoryButtons();
        createTextFields();
        setMenuButtons();
        setSidePanel();
        setFrame();
        displayFrame();
    }

    public void displayInventoryUpdater(int updateType) {
        /* Updates the panel to display content for user to update their inventory
    UPDATE TYPES:
        1) Add a product
        2) Remove a product
        3) Update quantity of product */
        String[] updateStrings = controller.getUpdateStrings(updateType); // update strings for title and corresponding labels for textfields

        // Clear previous content and setup layout
        contentPanel.removeAll();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Set title
        JLabel titleLabel = new JLabel(updateStrings[0]);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Update font and size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center-align the title
        contentPanel.add(titleLabel, gbc);

        // Display label and text field for productID section
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

        if (updateType == 1 || updateType == 3) { // display quantity section for add product and update quantity
            displayQuantitySection(gbc, updateStrings);
        }

        // Update the confirm button based on updateType
        if (updateType == 1) { // add product button
            displayAddProductButton(gbc);
        } else if (updateType == 2) { // remove product button
            displayRemoveProductButton(gbc);
        } else if (updateType == 3) { // update quantity button
            displayUpdateQuantityButton(gbc);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void setMenuButtons() {
        // Calls the methods of each menu button
        addProductButton.setEnabled(false);
        removeProductButton.setEnabled(false);
        updateQuantityButton.setEnabled(false);
        openInventoryPanel(); // Button 1: View Inventory Menu Button
        openAddProductPanel(); // Button 2: Add a product button
        openRemoveProductPanel(); // Button 3: Remove a product button
        openUpdateQuantityPanel(); // Button 4: Update quantity of a product button
        openCarCataloguePanel(); // Button 5: View car catalogue Button
        // Button 6: Exit Program
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program with a status code of 0
            }
        });
    }

    public void openInventoryPanel() {
        // Displays panel contents relative to the users inventory and creating an order 
        viewInventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Enable/disable buttons so nothing is accidentally clicked
                addProductButton.setEnabled(false);
                removeProductButton.setEnabled(false);
                updateQuantityButton.setEnabled(false);

                inventoryArray = controller.getInventory(); // get current users inventory from DB as an inventory obj array
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
                    inventoryTableModel.addRow(rowData); // add current obj to corresponding row
                }

                // turn table model into table and display users current inventory
                inventoryTable = new JTable(inventoryTableModel);
                displayTable(inventoryTable);

                if (inventoryArray.length > 0) { // if items in inventory then display create order button
                    displayOrderButton();
                }
                if (createOrderButton.getActionListeners().length == 0) {
                    createOrderButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            frame.dispose(); // Dispose the current frame
                            CreateOrderGUI createOrderGUI = new CreateOrderGUI(controller); // create a new CreateOrderGUI window
                        }
                    });
                }
            }
        });
    }

    public void openAddProductPanel() {
        // Displays panel contents relative to adding a product into the user's inventory
        addProductMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Enable/disable buttons
                addProductButton.setEnabled(true);
                removeProductButton.setEnabled(false);
                updateQuantityButton.setEnabled(false);
                displayInventoryUpdater(1);

                if (addProductButton.getActionListeners().length == 0) {
                    addProductButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // get values from text fields
                            String productID = productIDTextField.getText().toUpperCase();
                            String quantityText = quantityTextField.getText();
                            int quantity = 0;

                            if (controller.verifyAddingInput(productID, quantityText)) { // if user's input is valid
                                quantity = Integer.parseInt(quantityText); // change quantityText to an int
                                controller.addProduct(productID, quantity); // add a product
                            }

                            resetTextFields();
                        }
                    });
                }
            }
        });
    }

    public void openRemoveProductPanel() {
        // Displays panel contents relative to removing a product from the user's inventory
        removeProductMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Enable/disable buttons
                addProductButton.setEnabled(false);
                removeProductButton.setEnabled(true);
                updateQuantityButton.setEnabled(false);
                displayInventoryUpdater(2);

                if (removeProductButton.getActionListeners().length == 0) {
                    removeProductButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // get values from text fields
                            String productID = productIDTextField.getText().toUpperCase();

                            if (controller.verifyRemovingInput(productID)) { // if user's input is valid
                                controller.removeProduct(productID); // remove a product
                            }

                            resetTextFields();
                        }
                    });
                }
            }
        });
    }

    public void openUpdateQuantityPanel() {
        // Displays panel contents relative to updating a product's quantity in the user's inventory
        updateQuantityMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Enable/disable buttons
                addProductButton.setEnabled(false);
                removeProductButton.setEnabled(false);
                updateQuantityButton.setEnabled(true);
                displayInventoryUpdater(3); // gets contents for panel

                if (updateQuantityButton.getActionListeners().length == 0) {
                    updateQuantityButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // get values from text fields
                            String productID = productIDTextField.getText().toUpperCase();
                            String quantityText = quantityTextField.getText();
                            int quantity = 0;

                            if (controller.verifyUpdatingInput(productID, quantityText)) { // if user's input is valid
                                quantity = Integer.parseInt(quantityText); // change quantityText to an int
                                controller.updateProductQuantity(productID, quantity); // update quantity of a product
                            }

                            resetTextFields();
                        }
                    });
                }
            }
        });
    }

    public void openCarCataloguePanel() {
        // Displays panel which displays a table of all car products to select from
        if (viewCarProductsButton.getActionListeners().length == 0) {

            viewCarProductsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Enable/disable buttons so nothing is accidentally clicked
                    addProductButton.setEnabled(false);
                    removeProductButton.setEnabled(false);
                    updateQuantityButton.setEnabled(false);

                    if (!carCatalogueDisplayed) {
                        carCatalogueArray = controller.getCarCatalogue(); // retrieve car catalogue records from DB and store in array

                        for (CarCatalogue item : carCatalogueArray) { // loop through car catalogue array
                            Object[] rowData = new Object[]{
                                item.getProductName(),
                                item.getProductBrand(),
                                item.getProductPrice(),
                                item.getProductType()
                            };
                            carCatalogueTableModel.addRow(rowData); // add current obj to corresponding row
                        }

                        carCatalogueTable = new JTable(carCatalogueTableModel);
                        displayTable(carCatalogueTable);
                        carCatalogueDisplayed = true;
                    }
                }
            });
        }
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

    public void displayAddProductButton(GridBagConstraints gbc) {
        // Display the add product to inventory
        addProductButton.setVisible(true);
        removeProductButton.setVisible(false);
        updateQuantityButton.setVisible(true);

        addProductButton.setText("add product(s)");
        addProductButton.setFont(new Font("Arial", Font.BOLD, 18)); // Update font and size
        addProductButton.setBackground(new Color(0, 150, 0)); // Darker green color

        addProductButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center-align the button
        gbc.ipady = 20; // Increase button height
        contentPanel.add(addProductButton, gbc);
        addProductButton.setBorder(null);
    }

    public void displayRemoveProductButton(GridBagConstraints gbc) {
        addProductButton.setVisible(false);
        removeProductButton.setVisible(true);
        updateQuantityButton.setVisible(true);

        removeProductButton.setText("remove product");
        removeProductButton.setFont(new Font("Arial", Font.BOLD, 18)); // Update font and size
        removeProductButton.setBackground(Color.RED);
        removeProductButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center-align the button
        gbc.ipady = 20; // Increase button height
        contentPanel.add(removeProductButton, gbc);
        removeProductButton.setBorder(null);
    }

    public void displayUpdateQuantityButton(GridBagConstraints gbc) {
        addProductButton.setVisible(false);
        removeProductButton.setVisible(false);
        updateQuantityButton.setVisible(true);

        updateQuantityButton.setText("update quantity");
        updateQuantityButton.setFont(new Font("Arial", Font.BOLD, 18)); // Update font and size
        updateQuantityButton.setBackground(Color.BLUE);
        updateQuantityButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center-align the button
        gbc.ipady = 20; // Increase button height
        contentPanel.add(updateQuantityButton, gbc);
        updateQuantityButton.setBorder(null);
    }

    public void displayQuantitySection(GridBagConstraints gbc, String[] updateStrings) {
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

    public void resetTextFields() {
        productIDTextField.setText("");
        quantityTextField.setText("");
    }

    public void setFrame() {
        frame.setSize(1400, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(sidePanel, BorderLayout.WEST);
    }

    public void createTables() {
        // Create inventory and car catalogue table model
        this.inventoryTableModel = new DefaultTableModel();
        this.inventoryTableModel.setColumnIdentifiers(new String[]{"Product Name", "Product Brand", "Product Price", "Product Type", "Product Quantity"});

        this.carCatalogueTableModel = new DefaultTableModel();
        this.carCatalogueTableModel.setColumnIdentifiers(new String[]{"Product Name", "Product Brand", "Product Price", "Product Type"});
    }

    public void createTextFields() {
        // Create text fields for manipulating inventory
        this.productIDTextField = new JTextField();
        this.quantityTextField = new JTextField();
    }

    public void createMenuButtons() {
        // Initalise menu buttons that belong in side panel 
        this.viewInventoryButton = createStyledButton("View Inventory", buttonFont, buttonColor);
        this.viewCarProductsButton = createStyledButton("Car Products Catalogue", buttonFont, buttonColor);
        this.addProductMenuButton = createStyledButton("Add Product", buttonFont, buttonColor);
        this.removeProductMenuButton = createStyledButton("Remove Product", buttonFont, buttonColor);
        this.updateQuantityMenuButton = createStyledButton("Update Quantity", buttonFont, buttonColor);
        this.exitButton = createStyledButton("Exit", buttonFont, buttonColor);
    }

    public void createUpdateInventoryButtons() {
        // Create buttons that update the inventory
        this.addProductButton = new JButton("");
        this.removeProductButton = new JButton("");
        this.updateQuantityButton = new JButton("");

        // button that takes current inventory to create an order
        this.createOrderButton = createStyledButton("Create Order", buttonFont, buttonColor);
        this.createOrderButton.setVisible(false);
    }

    public void setMenuButtonStyle() {
        // Set styling for side panel buttons
        this.buttonFont = new Font("Arial", Font.BOLD, 24);
        this.buttonColor = new Color(45, 120, 230);
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

    public void setSidePanel() {
        sidePanel.setBackground(new Color(40, 44, 52));
        sidePanel.setLayout(new GridBagLayout());

        // Add side panel contents
        addToSidePanel(sidePanel, viewInventoryButton, 0, 0);
        addToSidePanel(sidePanel, addProductMenuButton, 0, 1);
        addToSidePanel(sidePanel, removeProductMenuButton, 0, 2);
        addToSidePanel(sidePanel, updateQuantityMenuButton, 0, 3);
        addToSidePanel(sidePanel, viewCarProductsButton, 0, 4);
        addToSidePanel(sidePanel, exitButton, 0, 5);
    }

    private void displayOrderButton() {
        // When user opens the view inventory button then the add order button will appear
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
        // Frame to display contents
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
}
