package com.example.agriecommerce.model;

public class User {
    private String userId;

    private String name;

    public User() {} // Needed for Firebase

    public User(String name) {
        this.name = name;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
}
