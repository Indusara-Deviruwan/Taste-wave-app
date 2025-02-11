package com.example.tastewaveapp.model;

import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private List<FoodCart> foodItems;
    private double totalPrice;
    private String orderStatus;
    private String orderDate;
    private String deliveryAddress;

    // Constructor
    public Order(int orderId, int userId, List<FoodCart> foodItems, double totalPrice, String orderStatus, String orderDate, String deliveryAddress) {
        this.orderId = orderId;
        this.userId = userId;
        this.foodItems = foodItems;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
    }

    // Empty Constructor
    public Order() {}

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<FoodCart> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodCart> foodItems) {
        this.foodItems = foodItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
