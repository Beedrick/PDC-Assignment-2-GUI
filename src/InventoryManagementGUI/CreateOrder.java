package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */

public class CreateOrder {

    private String productName;
    private String productBrand;
    private double productPrice;
    private String productType;
    private int productQuantity;

    public CreateOrder(String productName, String productBrand, double productPrice, String productType, int productQuantity) {
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
