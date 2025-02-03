package com.example.tastewaveapp.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastewaveapp.R;
import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class RestaurantMapActivity extends AppCompatActivity {

    private BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    private MapView mapView; // Add MapView object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        // Initialize MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState); // Pass savedInstanceState for proper state restoration

        // Optional: Handle window insets for better padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomSheetBehavior
        FrameLayout bottomSheet = findViewById(R.id.restaurantBottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // Set the BottomSheet to be hidden initially
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Optionally, you can change the peek height
        bottomSheetBehavior.setPeekHeight(200);

        // Add logic to show the bottom sheet when needed
        // For example, on a map marker click
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart(); // Add onStart to ensure the map works after the activity starts
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume(); // Add onResume to ensure the map continues to show after the activity resumes
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause(); // Pause map when activity goes into background
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop(); // Stop map resources when activity is stopped
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy(); // Clean up map resources when the activity is destroyed
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory(); // Handle memory-related issues
    }
}
