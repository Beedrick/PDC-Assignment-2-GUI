package InventoryManagementGUI;

public class Inventory {
    private String productName;
    private String productBrand;
    private double productPrice;
    private String productType;
    private int productQuantity;

    public Inventory(String productName, String productBrand, double productPrice, String productType, int productQuantity) {
        this.productName = productName;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productType = productType;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getProductType() {
        return productType;
    }

    public int getProductQuantity() {
        return productQuantity;
    }
}

// OLD INVENTORY CODE 
/*import java.awt.Font;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import javax.swing.JTable;

//The Inventory class is responsible for dealing with updating user inventory files 
//it allows the program to access the users inventory text files and update quantity
//and remove items
public class Inventory {

    private String inventoryTxt;
    private Scanner scan = new Scanner(System.in);
    private HashMap<Integer, CarProduct> inventory2;
    private String currentUser;
    private CarProduct product;

    public Inventory(String currentUser) { // default constructor
        this.inventory2 = new HashMap<Integer, CarProduct>();
        this.currentUser = currentUser;

        setInventoryHashMap();
    }

    public Inventory() {
        this.inventory2 = new HashMap<Integer, CarProduct>();
        setInventoryHashMap();
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public HashMap<Integer, CarProduct> getInventory() {
        return this.inventory2;
    }

    //setInventoryHashMap initializes the inventory2 HashMap field 
    //with the contents of the users inventory text file
    public void setInventoryHashMap() {

        //File file = new File("UserProfiles/" + this.currentUser + '/' + this.currentUser + "Inventory.txt");
        File file = new File("resources/Admin/AdminInventory.txt");
        
        //checks if file exists
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                if (file.length() != 0) {
                    String line;
                    //block of code takes each line in the file and sepereates the String by comma
                    //and stores in String array called parts to be used to initialize a CarProduct Object
                    while ((line = br.readLine()) != null) {
                        //skip empty or whitespace-only lines
                        if (line.trim().isEmpty()) {
                            continue;
                        }
                        String[] parts = line.split(",");

                        // parse values
                        int id = Integer.parseInt(parts[0]);
                        String model = parts[1];
                        String brand = parts[2];
                        String type = parts[3];
                        double price = Double.parseDouble(parts[4]);
                        int quantity = Integer.parseInt(parts[5]);

                        CarProduct setProduct = new CarTire(id, model, brand, type, price, quantity);
                        this.inventory2.put(id, setProduct);
                    }
                } else {
                    //if the user inventory text file has no content i.e. file.length == 0 OR line.trim().isEmpty 
                    //returns true then inventory2 is initialized with an empty hashMap
                    this.inventory2 = new HashMap<Integer, CarProduct>();
                }

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else {
            //calls the inventoryToTxt method which writes to the file and creates 
            //a txt file for the user if the file didnt already exist
            inventoryToTxt();
            System.out.println("> New User text file created.");
        }
    }

    //checks weather the users inventory text file already contains a specific car product
    //returns true if it does and false if not
    public boolean hasProduct(CarProduct prod) {
        boolean hasProd = false;
        for (HashMap.Entry<Integer, CarProduct> entry : inventory2.entrySet()) {
            CarProduct product = entry.getValue();
            if (product.productID == prod.productID) {
                hasProd = true;
            }

        }

        return hasProd;
    }

    //addProduct is responsible for adding new car products to the users inventory txt file
    //and if the user already has that product it adds on to the quantity (+=)
    public void addProduct() {

        int productToAdd = 0;
        String x = "";

        //prompts user to input a number to select which product type to add to inventory
        while (true) {
            System.out.println("Select the product type you wish to add: ");

            try {
                productToAdd = scan.nextInt();
                break; // break out of the loop if input is valid
            } catch (InputMismatchException e) {
                //handle non-integer input
                System.out.println("> Invalid input. Please enter a number.");
                scan.nextLine(); //consume the invalid input
                continue;
            }
        }

        //checks which number user selected and initializes the CarProduct abstract 
        //Object with one of the subclasses based on user selection
        switch (productToAdd) {
            case 1:
                this.product = new CarTire();
                x = "Tire";
                break;
            case 2:
                this.product = new CarSideMirror();
                x = "Side Mirror";
                break;
            case 3:
                this.product = new CarEngine();
                x = "Engine";
                break;
            case 4:
                this.product = new CarHeadLights();
                x = "Head Lights";
                break;
            case 5:
                return;
            default:
                System.out.println("> Invalid choice. Please try again.");
                addProduct();
                return;
        }

        //prompts user to enter a number to select the specific car product item by id 
        while (true) {
            System.out.println(product.toString());
            System.out.println("Select the " + x + " you want to add to inventory based on ID: ");

            try {
                productToAdd = scan.nextInt();
                break; // break out of the loop if input is valid
            } catch (InputMismatchException e) {
                // Handle non-integer input
                System.out.println("> Invalid input. Please enter a number.");
                scan.nextLine(); //consume the invalid input
                continue;
            }
        }

        //assigns a temporary CarProduct object with the car product slected by the user 
        CarProduct tempProd = product.getProductByID(productToAdd);

        //checks weather tempProd is an existing product and that it is not outside the range of 
        //choices. The user can put in any number and tempProd will be initialized with nulls and 
        //0's which is why it is checked for possible null values.
        if (tempProd != null && tempProd.productModel != null) {
            if (hasProduct(tempProd)) {

                int quantityToAdd = 0;

                while (true) {
                    System.out.println("How many would you like to add: ");
                    try {
                        quantityToAdd = scan.nextInt();
                        break; // break out of the loop if input is valid
                    } catch (InputMismatchException e) {
                        // Handle non-integer input
                        System.out.println("> Invalid input. Please enter a number.");
                        scan.nextLine(); //consume the invalid input
                        continue;
                    }
                }

                inventory2.get(productToAdd).addQuantity(quantityToAdd);
            } else {
                int quantityToAdd = 0;

                while (true) {
                    System.out.println("How many would you like to add: ");
                    try {
                        quantityToAdd = scan.nextInt();
                        break; // break out of the loop if input is valid
                    } catch (InputMismatchException e) {
                        // Handle non-integer input
                        System.out.println("> Invalid input. Please enter a number.");
                        scan.nextLine(); //consume the invalid input
                        continue;
                    }
                }
                tempProd.quantity = quantityToAdd;
                inventory2.put(tempProd.productID, tempProd);
                System.out.println("> Added " + tempProd.productID + " to inventory");
            }

            inventoryToTxt();
        } else {
            System.out.println("> Item not found!");
        }
    }

    //removeProduct() method removes an entire item from the user inventory text file.
    //it does this by first removing the selected CarProduct from the inventory2 hashMap
    //and then printing the updated inventory2 hashMap to the users inventory txt file
    public void removeProduct() {

        if (inventory2.isEmpty()) {
            System.out.println("> You need at least 1 item in your inventory in order to remove an item.\n");
        } else {
            System.out.println(toString());
            System.out.println("\nRemove an item based on ID: ");
            int productToRemove = scan.nextInt();
            CarProduct tempProd = inventory2.get(productToRemove);

            if (tempProd != null) {
                inventory2.remove(productToRemove);
                System.out.println("> Removed " + tempProd.productModel + " " + tempProd.productBrand + " from inventory");
                inventoryToTxt();
            } else {
                System.out.println("> Item not found!");
            }
        }
    }

    //similar to the removeProduct() method, the adjustQuantity() method adjusts the quantity
    //of an existing car product stored in the user inventory txt file. This method finds the
    //user selected car product in the inventory2 hashMap and updates the quantity of that car
    //product according to user input. This method then prints the updated inventory2 hashMap to 
    //the users txt file
    public void adjustQuantity() {

        if (inventory2.isEmpty()) {
            System.out.println("> You need at least 1 item in your inventory in order to adjust the quantity of item.\n");
        } else {
            System.out.println(toString());
            System.out.println("\nSelect an item based on ID: ");
            int productToUpdate = scan.nextInt();

            CarProduct tempProd = inventory2.get(productToUpdate);

            if (tempProd != null) {
                System.out.println("Enter the quantity of " + tempProd.productModel + " " + tempProd.productBrand);
                int newQuantity = scan.nextInt();
                inventory2.get(productToUpdate).quantity = newQuantity; // overwrite the quanity based on user input
                System.out.println("> Updated " + tempProd.productModel + " " + tempProd.productBrand + " to a quantity of " + inventory2.get(productToUpdate).quantity);
                inventoryToTxt();
            } else {
                System.out.println("> Item not found!");
            }
        }
    }

    //inventoryToTxt() method is responsible for writing to the user inventory txt file.
    //It does this by overwritting an existing txt file with the current inventory2 hashMap 
    //and if the users inventory txt file doesnt already exist it creates a new text file.
    public void inventoryToTxt() {
        try {

            // makes sure user actually entered a file name
            File newUserInven = new File("UserProfiles/" + this.currentUser + "/" + this.currentUser + "Inventory.txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(newUserInven));

            String output = "";
            for (HashMap.Entry<Integer, CarProduct> entry : inventory2.entrySet()) {
                CarProduct product = entry.getValue();
                output += String.format(product.productID + "," + product.productModel + ","
                        + product.productBrand + "," + product.productType + "," + product.price + "," + product.quantity + "\n");
            }

            pw.println(output);
            //System.out.println("> Inventory added to file '" + newUserInven + "'");
            pw.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    //getInventoryTxt() method returns the string of the inventoryTxt field
    public String getInventoryTxt() {
        return this.inventoryTxt;
    }

    //print out the contents of the inventory2 hashMap in a formatted inventory style stores in a JTable for GUI
    public JTable toTable() {
        String[] columnNames = {"ID", "PRODUCT NAME", "BRAND", "PRICE", "TYPE", "QUANTITY"};

        Object[][] rowData = new Object[inventory2.size()][columnNames.length];
        int index = 0;
        for (HashMap.Entry<Integer, CarProduct> entry : inventory2.entrySet()) {
            CarProduct product = entry.getValue();
            rowData[index] = new Object[]{product.productID, product.productModel, product.productBrand,
                product.price, product.productType, product.quantity};
            index++;
        }

        JTable table = new JTable(rowData, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(20);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        return table;
    }

} */
