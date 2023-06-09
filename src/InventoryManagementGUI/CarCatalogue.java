package InventoryManagementGUI;

/**
 *
 * @author rocco + beedrix
 */
public class CarCatalogue {

    private String productName;
    private String productBrand;
    private double productPrice;
    private String productType;

    public CarCatalogue(String productName, String productBrand, double productPrice, String productType) {
        this.productName = productName;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productType = productType;
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
}
