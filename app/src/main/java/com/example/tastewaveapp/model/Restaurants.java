package com.example.tastewaveapp.model;

public class Restaurants {

    private String id;
    private String name;
    private String description;
    private String imageResId;

    public Restaurants(String id,String name, String description, String imageResId) {
        this.id= id;
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageResId() {
        return imageResId;
    }
}

