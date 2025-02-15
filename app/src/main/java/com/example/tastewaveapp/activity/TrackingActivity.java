package com.example.tastewaveapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.tastewaveapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView deliveryTimeTextView, deliveryDistanceTextView, deliveryStatusTextView;
    private Button refreshButton;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // Variables for destination location
    private double latitude;
    private double longitude;
    private ArrayList<double[]> restaurantLocations; // Each element: [latitude, longitude]

    // Tracking details (fallback values if needed)
    private double deliveryTime;
    private double deliveryDistance;
    private String deliveryStatus;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        // Initialize UI elements
        deliveryTimeTextView = findViewById(R.id.deliveryTimeTextView);
        deliveryDistanceTextView = findViewById(R.id.distanceTextView);
        deliveryStatusTextView = findViewById(R.id.deliveryStatusTextView);
        refreshButton = findViewById(R.id.refreshTrackingButton);

        // Initialize the location client.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Retrieve tracking data from the intent.
        // Try to get multiple restaurant locations (passed as an ArrayList of double arrays).
        restaurantLocations = (ArrayList<double[]>) getIntent().getSerializableExtra("restaurantLocations");
        if (restaurantLocations == null || restaurantLocations.isEmpty()) {
            // If no list is provided, fall back to individual latitude/longitude extras.
            latitude = getIntent().getDoubleExtra("latitude", 0);
            longitude = getIntent().getDoubleExtra("longitude", 0);
        }

        // Retrieve other tracking details.
        deliveryTime = getIntent().getDoubleExtra("deliveryTime", 0);
        deliveryDistance = getIntent().getDoubleExtra("deliveryDistance", 0);
        deliveryStatus = getIntent().getStringExtra("deliveryStatus");
        if (deliveryStatus == null) {
            deliveryStatus = "Pending";
        }

        // Set initial tracking details on the UI.
        deliveryTimeTextView.setText("Estimated Delivery Time: " + deliveryTime + " mins");
        deliveryDistanceTextView.setText("Distance: " + deliveryDistance + " km");
        deliveryStatusTextView.setText("Delivery Status: " + deliveryStatus);

        // Set up the map fragment.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("TrackingActivity", "Map fragment is null.");
        }

        // Set up the refresh button to simulate refreshing tracking details.
        refreshButton.setOnClickListener(v -> refreshTrackingDetails());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add restaurant marker(s) if available.
        if (restaurantLocations != null && !restaurantLocations.isEmpty()) {
            for (double[] loc : restaurantLocations) {
                if (loc.length >= 2) {
                    LatLng restaurantLatLng = new LatLng(loc[0], loc[1]);
                    mMap.addMarker(new MarkerOptions().position(restaurantLatLng).title("Restaurant Location"));
                }
            }
            // Center the camera on the first restaurant location.
            double[] firstLoc = restaurantLocations.get(0);
            LatLng firstRestaurantLatLng = new LatLng(firstLoc[0], firstLoc[1]);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstRestaurantLatLng, 14));
        } else if (latitude != 0 && longitude != 0) {
            // Fallback: use single coordinate.
            LatLng restaurantLatLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(restaurantLatLng).title("Restaurant Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatLng, 14));
        } else {
            Log.e("TrackingActivity", "No restaurant location provided.");
        }

        // Retrieve the phone's current location and update tracking details.
        getCurrentLocationAndUpdateTracking();
    }

    private void getCurrentLocationAndUpdateTracking() {
        // Check for location permission.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double currentLat = location.getLatitude();
                double currentLon = location.getLongitude();

                // Add a marker for the current location.
                LatLng currentLatLng = new LatLng(currentLat, currentLon);
                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Your Location"));

                // Determine destination: use the first restaurant location if available.
                double destLat, destLon;
                if (restaurantLocations != null && !restaurantLocations.isEmpty()) {
                    double[] firstLoc = restaurantLocations.get(0);
                    destLat = firstLoc[0];
                    destLon = firstLoc[1];
                } else if (latitude != 0 && longitude != 0) {
                    destLat = latitude;
                    destLon = longitude;
                } else {
                    Log.e("TrackingActivity", "No destination location available.");
                    return;
                }

                // Compute the distance between the current location and the destination.
                float[] results = new float[1];
                Location.distanceBetween(currentLat, currentLon, destLat, destLon, results);
                float distanceInMeters = results[0];
                double distanceInKm = distanceInMeters / 1000.0;

                // Compute estimated time based on an average speed (e.g., 40 km/h).
                double averageSpeedKmh = 40;
                double estimatedTimeInMinutes = (distanceInKm / averageSpeedKmh) * 60;

                // Update UI with computed values.
                deliveryDistanceTextView.setText("Distance: " + String.format("%.2f", distanceInKm) + " km");
                deliveryTimeTextView.setText("Estimated Delivery Time: " + String.format("%.0f", estimatedTimeInMinutes) + " mins");
            } else {
                Log.e("TrackingActivity", "Current location is null.");
            }
        });
    }

    /**
     * Simulates refreshing the tracking details.
     * In a real-world scenario, you might fetch updated live data.
     */
    private void refreshTrackingDetails() {
        // For demonstration, update the delivery status.
        deliveryStatus = "On the Way";
        deliveryStatusTextView.setText("Delivery Status: " + deliveryStatus);

        // Refresh current location and recalculate distance/time.
        getCurrentLocationAndUpdateTracking();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndUpdateTracking();
            } else {
                Log.e("TrackingActivity", "Location permission denied.");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
