package com.example.tastewaveapp.model;

import java.io.Serializable;

public class FoodCart implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;
    private String restaurantId;
    private String restaurantName;

    //
    private String foodName, foodRestaurantName;
    private int foodRestaurantId, foodQuantity;

    // Constructor with all parameters
    public FoodCart(int id, String name, String description, double price, int quantity, String imageUrl, String restaurantId, String restaurantName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public FoodCart(int id, String name, String description, double price, int quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public FoodCart(String foodName, int foodQuantity, int foodRestaurantId, String foodRestaurantName) {
        this.foodName = foodName;
        this.foodRestaurantName = foodRestaurantName;
        this.foodRestaurantId = foodRestaurantId;
        this.foodQuantity = foodQuantity;

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
        this.restaurantId = ""; // Default value
        this.restaurantName = ""; // Default value
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

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    // Method to increase quantity
    public void increaseQuantity() {
        this.quantity++;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodRestaurantName() {
        return foodRestaurantName;
    }

    public void setFoodRestaurantName(String foodRestaurantName) {
        this.foodRestaurantName = foodRestaurantName;
    }

    public int getFoodRestaurantId() {
        return foodRestaurantId;
    }

    public void setFoodRestaurantId(int foodRestaurantId) {
        this.foodRestaurantId = foodRestaurantId;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
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