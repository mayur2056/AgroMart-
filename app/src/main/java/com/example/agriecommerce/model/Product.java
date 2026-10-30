package com.example.agriecommerce.model;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;

    private String id;
    private String imageUrl;
    private String price;
    private String description;
    private float rating;
    private boolean inStock;
    private String category;
    private String farmName;  // Added farmName
    private int quantity;     // Added quantity
    private String userEmail; // Added userEmail

    // No-argument constructor (Required by Firebase)
    public Product() {
    }

    public Product(String id){
        this.id=id;
    }

    // Parameterized constructor
    public Product(String name, String imageUrl, String price, String description, float rating, boolean inStock,
                   String category, String farmName, int quantity, String userEmail) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.inStock = inStock;
        this.category = category;
        this.farmName = farmName;
        this.quantity = quantity;
        this.userEmail = userEmail;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.inStock = quantity > 0; // Automatically update inStock based on quantity
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
