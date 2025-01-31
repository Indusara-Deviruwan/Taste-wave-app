package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.RestaurantAdapter;
import com.example.tastewaveapp.model.Restaurants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private ListView listView;
    private TextView textViewWelcome;
    private List<Restaurants> restaurantList;
    private RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Restaurants restaurant = restaurantList.get(position);

            Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
            intent.putExtra("RESTAURANT_ID", restaurant.getId()); // Pass Firestore document ID
            intent.putExtra("RESTAURANT_NAME", restaurant.getName());
            intent.putExtra("RESTAURANT_DESCRIPTION", restaurant.getDescription());
            intent.putExtra("RESTAURANT_IMAGE", restaurant.getImageResId());
            startActivity(intent);
        });


        // Bottom navigation functionality
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
                return true;
            } else if (id == R.id.nav_payment) {
                startActivity(new Intent(HomeActivity.this, PaymentActivity.class));
                return true;
            } else if (id == R.id.nav_offers) {
                startActivity(new Intent(HomeActivity.this, OffersActivity.class));
                return true;
            }
            return false;
        });
    }

    private void fetchRestaurants() {
        CollectionReference restaurantsRef = db.collection("Restaurants");

        restaurantsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    restaurantList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        // Retrieve restaurant details
                        String restaurantId = document.getId(); // Get Firestore document ID
                        String name = document.getString("name");
                        String description = document.getString("description");
                        String imageResId = document.getString("imageResId");

                        // Create Restaurant object with Firestore ID
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
