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
    private MainMenuController controller;
    private Font buttonFont;
    private Color buttonColor;
    private JFrame frame;
    private JPanel sidePanel;
    private JButton viewInventoryButton;
    private JButton exitButton;
    private JButton viewCarProductsButton;
    private JButton addProductButton;
    private JButton removeProductButton;
    private JButton updateQuantityButton;
    private GridBagConstraints constraints;
    private static JPanel contentPanel; // Panel to display contents
    private DatabaseManager databaseManager;
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;  

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
        
        createTable();
        setFrame();
        setSidePanel();
        setButtons();
        displayFrame();
    }

    public void createTable() {
        this.inventoryTableModel = new DefaultTableModel();
        this.inventoryTableModel.setColumnIdentifiers(new String[]{"Product ID", "Product Number", "Product Brand", "Product Price", "Product Type", "Product Quantity"});
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
    
    public void displayUpdateInventory() {
    // Updates the panel to allow user to input info to add/remove products
        contentPanel.removeAll(); // Clear previous content
        
        JLabel updateLabel = new JLabel("Add a product");
    }

    public void displayTable(JTable table) {
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
                Inventory[] inventoryArray = controller.getInventory(); // get current users inventory from DB as an array
                inventoryTableModel.setRowCount(0); // clear inventory table

                // loop through currentUser inventory array
                for (Inventory item : inventoryArray) {
                    Object[] rowData = new Object[]{
                        item.getProductID(),
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
            }
        });

        // Box 2: Additional Button 2
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                displayUpdateInventory();
            }
        });
        // Box 3: Additional Button 3
        removeProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action for Button 3 to be set
            }
        }); 

        // Box 4: Additional Button 3
        updateQuantityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action for Button 3 to be set
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
}
