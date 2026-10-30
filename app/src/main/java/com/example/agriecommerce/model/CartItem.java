package com.example.agriecommerce.model;

import java.util.Objects;

public class CartItem {
    private String cartId; // New field for Firebase mapping
    private String userEmail; // New field for Firebase mapping
    private String productId;
    private String name;
    private String imageUrl;
    private String price;
    private int quantity;
    private String farmName;
    private boolean inStock;
    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
    // Default constructor required for Firebase
    public CartItem() {
    }

    public CartItem(String cartId, String userEmail, String productId, String name, String imageUrl, String price, int quantity) {
        this.cartId = cartId;
        this.userEmail = userEmail;
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    // ✅ Getters
    public String getCartId() { return cartId; }
    public String getUserEmail() { return userEmail; }
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // ✅ Setters (if needed)
    public void setCartId(String cartId) { this.cartId = cartId; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setProductId(String productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(String price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // ✅ equals() & hashCode() for RecyclerView DiffUtil efficiency
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return quantity == cartItem.quantity &&
                Objects.equals(cartId, cartItem.cartId) &&
                Objects.equals(userEmail, cartItem.userEmail) &&
                Objects.equals(productId, cartItem.productId) &&
                Objects.equals(name, cartItem.name) &&
                Objects.equals(imageUrl, cartItem.imageUrl) &&
                Objects.equals(price, cartItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, userEmail, productId, name, imageUrl, price, quantity);
    }
    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }
}
