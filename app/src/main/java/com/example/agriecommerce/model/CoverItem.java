package com.example.agriecommerce.model;

public class CoverItem {
    private String title;
    private String imageUrl;

    public CoverItem() {
        // Default constructor required for Firebase
    }

    public CoverItem(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
