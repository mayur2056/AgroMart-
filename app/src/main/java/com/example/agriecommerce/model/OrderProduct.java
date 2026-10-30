package com.example.agriecommerce.model;

public class OrderProduct {
    private String productName;
    private String productImage;
    private String productPrice;

    private int quantity;
    private String status;
    private String farmName;

    public OrderProduct() {
        // Default constructor required for Firebase
    }

    public OrderProduct(String productName, String productImage, String productPrice, String farmName,String status,int quantity ) {
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.farmName = farmName;
        this.quantity=quantity;
        this.status=status;
    }

    public String getProductName() { return productName; }
    public String getProductImage() { return productImage; }
    public String getProductPrice() { return productPrice; }
    public String getStatus() { return status; }
    public int getQuantity() { return quantity; }
    public void setStatus(String status) { this.status=status; }

    public String getFarmName() { return farmName; }
}
