package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.activity.RestaurantActivity;
import com.example.tastewaveapp.adapter.RestaurantAdapter;
import com.example.tastewaveapp.databasehelper.DatabaseHelper;
import com.example.tastewaveapp.model.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(v -> {
            // Navigate to CartActivity
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                // Navigate to Profile Activity
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_orders) {
                // Navigate to Orders Activity
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
                return true;
            } else if (id == R.id.nav_payment) {
                // Navigate to Payment Activity
                startActivity(new Intent(HomeActivity.this, PaymentActivity.class));
                return true;
            } else if (id == R.id.nav_offers) {
                // Navigate to Offers Activity
                startActivity(new Intent(HomeActivity.this, OffersActivity.class));
                return true;
            }
            return false;
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        databaseHelper.insertRestaurant("MR.Kottu","good kottu", R.drawable.start);

        // Fetch restaurants from the database
        restaurantList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllRestaurants();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESTAURANT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESTAURANT_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESTAURANT_DESCRIPTION));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESTAURANT_IMAGE));
                restaurantList.add(new Restaurant(id, name, description, imageResId));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Set up the adapter
        restaurantAdapter = new RestaurantAdapter(restaurantList, restaurant -> {
            // Handle restaurant click
            Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
            intent.putExtra("restaurant_id", restaurant.getId());
            intent.putExtra("restaurant_name", restaurant.getName());
            intent.putExtra("restaurant_description", restaurant.getDescription());
            intent.putExtra("restaurant_image", restaurant.getImageResId());
            startActivity(intent);
        });

        recyclerView.setAdapter(restaurantAdapter);


    }
}
