package com.example.tastewaveapp.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.model.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.appcompat.widget.SearchView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    private MapView mapView;
    private GoogleMap googleMap;
    private List<Restaurant> restaurantList;
    private List<Restaurant> filteredRestaurantList;
    private FirebaseFirestore db;

    private TextView restaurantName, restaurantRating, restaurantDescription;
    private ImageView restaurantImage;
    private SearchView searchView;  // Use SearchView instead of EditText
    private Button submitSearchButton;  // Remove this if you don't need it anymore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        // Initialize the views
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        FrameLayout bottomSheet = findViewById(R.id.restaurantBottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        restaurantName = findViewById(R.id.restaurantName);
        restaurantRating = findViewById(R.id.restaurantRating);
        restaurantDescription = findViewById(R.id.restaurantDescription);
        restaurantImage = findViewById(R.id.restaurantImage);

        searchView = findViewById(R.id.search_view);  // Initialize the SearchView
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setQueryHint("Search Restaurants...");


        db = FirebaseFirestore.getInstance();
        restaurantList = new ArrayList<>();
        filteredRestaurantList = new ArrayList<>();
        fetchRestaurants();

        // Set listener to filter restaurants based on search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRestaurants(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRestaurants(newText);
                return true;
            }
        });
    }

    private void fetchRestaurants() {
        CollectionReference restaurantsRef = db.collection("Restaurants");

        restaurantsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    restaurantList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        String restaurantId = document.getId();
                        String name = document.getString("name");
                        String description = document.getString("description");
                        String imageResId = document.getString("imageResId"); // Firebase storage URL or file name
                        String rating = document.getString("rating");
                        GeoPoint location = document.getGeoPoint("location");

                        Restaurant restaurant = new Restaurant(restaurantId, name, description, imageResId, rating, location);
                        restaurantList.add(restaurant);
                    }

                    // Initially show all restaurants
                    filteredRestaurantList.addAll(restaurantList);
                    if (googleMap != null) {
                        addMarkersToMap();
                        if (!filteredRestaurantList.isEmpty()) {
                            GeoPoint firstLocation = filteredRestaurantList.get(0).getLocation();
                            if (firstLocation != null) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 15));
                            }
                        }
                    }

                    if (restaurantList.isEmpty()) {
                        Toast.makeText(this, "No restaurants found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Restaurants loaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("RestaurantMapActivity", "Error fetching restaurants", e);
                    Toast.makeText(this, "Failed to load restaurants", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (!filteredRestaurantList.isEmpty()) {
            addMarkersToMap();
            GeoPoint firstLocation = filteredRestaurantList.get(0).getLocation();
            if (firstLocation != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 15));
            }
        }
    }

    private void addMarkersToMap() {
        googleMap.clear(); // Clear previous markers
        boolean restaurantFound = false;

        for (Restaurant restaurant : filteredRestaurantList) {
            GeoPoint geoPoint = restaurant.getLocation();
            if (geoPoint != null) {
                LatLng position = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(restaurant.getName());

                // Highlight the restaurant's marker if it matches the search query
                if (filteredRestaurantList.contains(restaurant)) {
                    // Highlight the restaurant's marker (e.g., using a different color)
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    restaurantFound = true;

                    // Move camera to the searched restaurant location
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                Marker marker = googleMap.addMarker(markerOptions);
                marker.setTag(restaurant);
            }
        }

        googleMap.setOnMarkerClickListener(marker -> {
            Restaurant restaurant = (Restaurant) marker.getTag();
            if (restaurant != null) {
                showRestaurantDetails(restaurant);
            }
            return false;
        });

        // Check if any restaurant was found, and if not, show the appropriate toast
        if (!restaurantFound) {
            Toast.makeText(this, "No matching restaurants found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRestaurantDetails(Restaurant restaurant) {
        restaurantName.setText(restaurant.getName());
        restaurantRating.setText("Rating: " + restaurant.getRating());
        restaurantDescription.setText(restaurant.getDescription());
        // Load image from Firebase storage (assuming imageResId is the Firebase storage URL or file name)
        String imageUrl = restaurant.getImageResId();
        Glide.with(this)
                .load(imageUrl)  // Load the image using Glide
                .into(restaurantImage);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void filterRestaurants(String query) {
        // Filter restaurants based on the query
        if (query.isEmpty()) {
            filteredRestaurantList.clear();
            filteredRestaurantList.addAll(restaurantList);
        } else {
            filteredRestaurantList = restaurantList.stream()
                    .filter(restaurant -> restaurant.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Check if the filtered list is empty and show appropriate Toast message
        if (filteredRestaurantList.isEmpty()) {
            Toast.makeText(this, "No restaurants found matching your search", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, filteredRestaurantList.size() + " restaurant(s) found", Toast.LENGTH_SHORT).show();
        }

        // Update the map markers based on the filtered list
        if (googleMap != null) {
            addMarkersToMap();
        }
    }

    @Override protected void onStart() { super.onStart(); mapView.onStart(); }
    @Override protected void onResume() { super.onResume(); mapView.onResume(); }
    @Override protected void onPause() { super.onPause(); mapView.onPause(); }
    @Override protected void onStop() { super.onStop(); mapView.onStop(); }
    @Override protected void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}
