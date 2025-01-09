package com.example.tastewaveapp.model;

public class Restaurant {
    private int id;
    private String name;
    private String description;
    private int imageResId;

    public Restaurant(int id,String name, String description, int imageResId) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }
}

