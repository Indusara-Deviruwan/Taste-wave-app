package com.example.tastewaveapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;  // Correct import

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private FloatingActionButton zoomInButton, zoomOutButton, locationButton;
    private float currentZoomLevel = 15f; // Default zoom level

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
        searchView.setQueryHint("Search Restaurants...");

        zoomInButton = findViewById(R.id.zoomInButton);
        zoomOutButton = findViewById(R.id.zoomOutButton);
        locationButton = findViewById(R.id.locationButton);

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

        // Set listeners for zoom buttons
        zoomInButton.setOnClickListener(v -> zoomIn());
        zoomOutButton.setOnClickListener(v -> zoomOut());

        // Set listener for location button (to center the map on user's current location)
        locationButton.setOnClickListener(v -> centerOnUserLocation());
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
                                        new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), currentZoomLevel));
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
                        new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), currentZoomLevel));
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
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    restaurantFound = true;

                    // Move camera to the searched restaurant location
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, currentZoomLevel));
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

        if (!restaurantFound) {
            Toast.makeText(this, "No matching restaurants found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRestaurantDetails(Restaurant restaurant) {
        restaurantName.setText(restaurant.getName());
        restaurantRating.setText("Rating: " + restaurant.getRating());
        restaurantDescription.setText(restaurant.getDescription());
        String imageUrl = restaurant.getImageResId();
        Glide.with(this)
                .load(imageUrl)
                .into(restaurantImage);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void filterRestaurants(String query) {
        if (query.isEmpty()) {
            filteredRestaurantList.clear();
            filteredRestaurantList.addAll(restaurantList);
        } else {
            filteredRestaurantList = restaurantList.stream()
                    .filter(restaurant -> restaurant.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (filteredRestaurantList.isEmpty()) {
            Toast.makeText(this, "No restaurants found matching your search", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, filteredRestaurantList.size() + " restaurant(s) found", Toast.LENGTH_SHORT).show();
        }

        if (googleMap != null) {
            addMarkersToMap();
        }
    }

    private void zoomIn() {
        if (googleMap != null) {
            currentZoomLevel++;
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel));
        }
    }

    private void zoomOut() {
        if (googleMap != null) {
            currentZoomLevel--;
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel));
        }
    }

    private void centerOnUserLocation() {
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), currentZoomLevel));
        }
    }

    @Override protected void onStart() { super.onStart(); mapView.onStart(); }
    @Override protected void onResume() { super.onResume(); mapView.onResume(); }
    @Override protected void onPause() { super.onPause(); mapView.onPause(); }
    @Override protected void onStop() { super.onStop(); mapView.onStop(); }
    @Override protected void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}
