package com.example.agriecommerce.model;

import com.google.firebase.firestore.auth.User;

import java.io.Serializable;

public class Users implements Serializable {
    private String name;
    private String phoneNumber;
    private String userId;
    private String email;
    private String userType;
    private String userImage;
    private String password;
    private String approval;



    public Users(String userId){
        this.userId=userId;
    }
    // Constructor
    public Users(String name, String phoneNumber, String userId, String email, String userType, String userImage, String password,String approval) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.email = email;
        this.userType = userType;
        this.userImage = userImage;
        this.password = password;
        this.approval= approval;
    }

    public Users() {
    }

//    public Users(String key) {
//    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getPassword() {
        return password;
    }
    public String getApproval(){return approval;}
}
