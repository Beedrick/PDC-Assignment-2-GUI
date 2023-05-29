/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManagementGUI;

import java.util.ArrayList;

//CarProduct is an abstract class that stores the attributes shared by all car products. 
//It has get and set methods for each field
public abstract class CarProduct {

    protected int productID;
    protected String productModel;
    protected String productBrand;
    protected String productType;
    protected double price;
    protected int quantity;

    //an arraylist storing multiple carproducts used by subclasses 
    //for user inventory data storage or printing the menu using data 
    //from CarProduct Text files
    public ArrayList<CarProduct> prodList;

    //abstract method inherited by subclasses to retrive product by ID
    public abstract CarProduct getProductByID(int prodID);

    //adds onto the quantity of a product in inventory
    public void addQuantity(int quantity) { 
        this.quantity += quantity;
    }
    
    //getter and setter methods
    public int getProductId() {
        return this.productID;
    }

    public void setProductId(int prodId) {
        this.productID = prodId;
    }

    public String getProductModel() {
        return this.productModel;
    }

    public void setProductModel(String prodModel) {
        this.productModel = prodModel;
    }

    public String getProductBrand() {
        return this.productBrand;
    }

    public void setProductBrand(String prodBrand) {
        this.productBrand = prodBrand;
    }
    
      public String getProductType() {
        return this.productType;
    }

    public void setProductType(String prodType) {
        this.productType = prodType;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quant) {
        this.quantity = quant;
    }
}