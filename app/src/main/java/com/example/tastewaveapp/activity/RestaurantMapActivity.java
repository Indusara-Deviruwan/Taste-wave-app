package com.example.tastewaveapp.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.model.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class RestaurantMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    private MapView mapView;
    private GoogleMap googleMap;
    private List<Restaurant> restaurantList;
    private FirebaseFirestore db;

    private TextView restaurantName, restaurantRating, restaurantDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        FrameLayout bottomSheet = findViewById(R.id.restaurantBottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        restaurantName = findViewById(R.id.restaurantName);
        restaurantRating = findViewById(R.id.restaurantRating);
        restaurantDescription = findViewById(R.id.restaurantDescription);

        db = FirebaseFirestore.getInstance();
        restaurantList = new ArrayList<>();
        loadRestaurantsFromFirestore();
    }

    private void loadRestaurantsFromFirestore() {
        db.collection("Restaurants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    restaurantList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        restaurantList.add(restaurant);
                    }
                    if (googleMap != null) {
                        addMarkersToMap();
                    }
                })
                .addOnFailureListener(e -> Log.e("RestaurantMapActivity", "Error loading restaurants", e));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (!restaurantList.isEmpty()) {
            addMarkersToMap();
        }
    }

    private void addMarkersToMap() {
        for (Restaurant restaurant : restaurantList) {
            GeoPoint geoPoint = restaurant.getLocation();
            if (geoPoint != null) {
                LatLng position = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(restaurant.getName()));
                marker.setTag(restaurant);
            }
        }

        if (!restaurantList.isEmpty()) {
            GeoPoint firstLocation = restaurantList.get(0).getLocation();
            if (firstLocation != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 12));
            }
        }

        googleMap.setOnMarkerClickListener(marker -> {
            Restaurant restaurant = (Restaurant) marker.getTag();
            if (restaurant != null) {
                showRestaurantDetails(restaurant);
            }
            return false;
        });
    }

    private void showRestaurantDetails(Restaurant restaurant) {
        restaurantName.setText(restaurant.getName());
        restaurantRating.setText("Rating: " + restaurant.getRating());
        restaurantDescription.setText(restaurant.getDescription());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override protected void onStart() { super.onStart(); mapView.onStart(); }
    @Override protected void onResume() { super.onResume(); mapView.onResume(); }
    @Override protected void onPause() { super.onPause(); mapView.onPause(); }
    @Override protected void onStop() { super.onStop(); mapView.onStop(); }
    @Override protected void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}
