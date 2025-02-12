package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.RestaurantAdapter;
import com.example.tastewaveapp.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Menu menu;

    private ListView listView;
    private TextView textViewWelcome;
    private ImageButton buttonLocation;
    private List<Restaurant> restaurantList;
    private RestaurantAdapter restaurantAdapter;
    private Toolbar toolbar; // Toolbar reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup Bottom Navigation
        setupBottomNavigation();

        buttonLocation = findViewById(R.id.btn_location);
        listView = findViewById(R.id.listView);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        //textViewWelcome.setText(user != null ? "Welcome, " + user.getEmail() : "Click here to log in");

        restaurantList = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(this, restaurantList);
        listView.setAdapter(restaurantAdapter);

        fetchRestaurants();

        // Map Button Click Listener
        buttonLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, RestaurantMapActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Restaurant restaurant = restaurantList.get(position);

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
                        String rating = document.getString("rating");
                        GeoPoint location = document.getGeoPoint("location");

                        Restaurant restaurant = new Restaurant(restaurantId, name, description, imageResId, rating, location);
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

    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu); // Make sure toolbar_menu.xml exists
        this.menu = menu; // Store the menu reference
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateMenu(menu);
        return true;
    }

    private void updateMenu(Menu menu) {
        MenuItem loginMenuItem = menu.findItem(R.id.action_login);
        if (loginMenuItem != null) {
            if (user != null) {
                loginMenuItem.setTitle("Sign Out");
            } else {
                loginMenuItem.setTitle("Login");
            }
        }
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_login) {
            if (user != null) { // If user is logged in, sign them out
                FirebaseAuth.getInstance().signOut(); // Firebase sign out
                user = null; // Reset user reference
                invalidateOptionsMenu(); // Refresh the menu to update the title
                Toast.makeText(this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
            } else { // If user is not logged in, navigate to login screen
                startActivity(new Intent(this, LogInActivity.class));
            }
            return true;
        }
        if (item.getItemId() == R.id.action_notification) {
            Toast.makeText(this, "Notification Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, OffersActivity.class)); // Navigate to LoginActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
