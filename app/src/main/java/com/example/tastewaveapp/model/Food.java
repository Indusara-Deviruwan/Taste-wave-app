package com.example.tastewaveapp.model;

public class Food {

    private String id;
    private String name;
    private String description;
    private String imageResId;
    private String price;
    private String restaurantId;


    public Food(String id, String name, String description, String imageResId, String price, String restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.price = price;
        this.restaurantId = restaurantId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
