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
import java.util.Objects;

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

        // Initialize UI components
        listView = findViewById(R.id.listView);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if the user is signed in
        user = auth.getCurrentUser();
        if (user == null) {
            textViewWelcome.setText("Click here to log in");
        } else {
            textViewWelcome.setText("Welcome, " + user.getEmail());
        }

        // Initialize restaurant list and adapter
        restaurantList = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(this, restaurantList);
        listView.setAdapter(restaurantAdapter);

        // Fetch restaurants from Firestore
        fetchRestaurants();

        // Handle list item click
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Restaurants restaurant = restaurantList.get(position);

            // Navigate to RestaurantActivity
            Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
            intent.putExtra("RESTAURANT_ID", restaurant.getId());
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
        // Reference to the Firestore collection
        CollectionReference restaurantsRef = db.collection("Restaurants");

        restaurantsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    restaurantList.clear(); // Clear the list before adding new data
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        // Create Restaurant object from Firestore document
                        Restaurants restaurant = new Restaurants(
                                Objects.requireNonNull(document.getLong("id")).intValue(),
                                document.getString("name"),
                                document.getString("description"),
                                document.getString("imageResId") // Ensure this matches your Firestore field name
                        );

                        restaurantList.add(restaurant); // Add restaurant to the list
                    }

                    // Notify adapter of data changes
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
