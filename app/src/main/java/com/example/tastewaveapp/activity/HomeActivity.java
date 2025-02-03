package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.RestaurantAdapter;
import com.example.tastewaveapp.model.Restaurants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private ListView listView;
    private TextView textViewWelcome;
    private Button mapButton;
    private List<Restaurants> restaurantList;
    private RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setup Bottom Navigation
        setupBottomNavigation();

        mapButton = findViewById(R.id.buttonMap);

        listView = findViewById(R.id.listView);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        user = auth.getCurrentUser();
        textViewWelcome.setText(user != null ? "Welcome, " + user.getEmail() : "Click here to log in");

        restaurantList = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(this, restaurantList);
        listView.setAdapter(restaurantAdapter);

        fetchRestaurants();

        //map button
        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(this,RestaurantMapActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Restaurants restaurant = restaurantList.get(position);

            Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
            intent.putExtra("RESTAURANT_ID", restaurant.getId()); // Pass Firestore document ID
            intent.putExtra("RESTAURANT_NAME", restaurant.getName());
            intent.putExtra("RESTAURANT_DESCRIPTION", restaurant.getDescription());
            intent.putExtra("RESTAURANT_IMAGE", restaurant.getImageResId());
            startActivity(intent);
        });


    }

    @Override
    protected String getToolbarTitle() {
        return "Taste Wave";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return R.id.nav_home; // Highlight "Home" in bottom navigation
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
                        String imageResId = document.getString("imageResId");

                        Restaurants restaurant = new Restaurants(restaurantId, name, description, imageResId);
                        restaurantList.add(restaurant);
                    }

                    restaurantAdapter.notifyDataSetChanged();

                    if (restaurantList.isEmpty()) {
                        Toast.makeText(this, "No restaurants found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Restaurants loaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching restaurants: ", e);
                    Toast.makeText(this, "Failed to load restaurants", Toast.LENGTH_SHORT).show();
                });
    }
}
