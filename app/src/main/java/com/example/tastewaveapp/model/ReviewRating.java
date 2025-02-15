package com.example.tastewaveapp.model;

public class ReviewRating {
    private String restaurantId;
    private String restaurantName;
    private String foodId;
    private String foodName;
    private float rating;
    private String reviewText;
    private String photoUri;

    // Constructors
    public ReviewRating() {
    }

    public ReviewRating(String restaurantId, String restaurantName, String foodId, String foodName, float rating, String reviewText, String photoUri) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.foodId = foodId;
        this.foodName = foodName;
        this.rating = rating;
        this.reviewText = reviewText;
        this.photoUri = photoUri;
    }

    // Getters and Setters
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

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
