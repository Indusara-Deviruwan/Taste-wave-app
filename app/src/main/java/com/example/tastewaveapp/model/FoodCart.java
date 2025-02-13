package com.example.tastewaveapp.model;

import java.io.Serializable;

public class FoodCart implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;

    // Constructor with all parameters
    public FoodCart(int id, String name, String description, double price, int quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Empty Constructor
    public FoodCart() {}

    // New constructor for name and quantity only
    public FoodCart(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.id = 0;  // Default value for id
        this.description = "";  // Default value
        this.price = 0.0;  // Default value
        this.imageUrl = "";  // Default value
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Method to increase quantity
    public void increaseQuantity() {
        this.quantity++;
    }

    // Method to decrease quantity
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    // Method to calculate total price for this item
    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}
