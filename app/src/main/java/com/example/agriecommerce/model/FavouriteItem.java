package com.example.agriecommerce.model;

public class FavouriteItem {
    private String favId;
    private String name;
    private String imageUrl;
    private String price;
    private String userId;
    private String userEmail;
    private String farmName;
    private boolean inStock;
    // Default constructor required for Firebase
    public FavouriteItem() {
    }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getUserEmail() { return userEmail; }
    public void setFarmName(String farmName) { this.farmName = farmName; }
    public FavouriteItem(String favId, String name, String imageUrl, String price, String userId,String userEmail) {
        this.favId = favId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.userId = userId;
        this.userEmail=userEmail;
    }

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
    public String getFarmName() {
        return farmName;
    }


}
