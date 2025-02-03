package com.example.tastewaveapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class Restaurant {

    private String id;
    private String name;
    private String description;
    private String imageResId;
    private String rating;
    private GeoPoint location;

    public Restaurant() {
        // Default constructor required for Firestore deserialization
    }

    public Restaurant(String id, String name, String description, String imageResId, String rating, GeoPoint location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.rating = rating;
        this.location = location;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public LatLng getLatLng() {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
