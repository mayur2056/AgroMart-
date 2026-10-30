package com.example.agriecommerce.model;

import java.util.List;

public class Order {
    private String orderID;
    private String orderDate;
    private String orderTime;
    private List<OrderProduct> products;

    private Double latitude;
    private Double longitude;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(String orderID, String orderDate, String orderTime, List<OrderProduct> products,Double latitude,Double longitude) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.products = products;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getOrderID() { return orderID; }
    public String getOrderDate() { return orderDate; }
    public String getOrderTime() { return orderTime; }
    public List<OrderProduct> getProducts() { return products; }
}
