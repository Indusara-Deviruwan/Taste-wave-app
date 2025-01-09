package com.example.tastewaveapp.activity;

import static com.example.tastewaveapp.databasehelper.DatabaseHelper.COLUMN_FOOD_DESCRIPTION;
import static com.example.tastewaveapp.databasehelper.DatabaseHelper.COLUMN_FOOD_ID;
import static com.example.tastewaveapp.databasehelper.DatabaseHelper.COLUMN_FOOD_IMAGE;
import static com.example.tastewaveapp.databasehelper.DatabaseHelper.COLUMN_FOOD_NAME;
import static com.example.tastewaveapp.databasehelper.DatabaseHelper.COLUMN_FOOD_PRICE;
import static com.example.tastewaveapp.databasehelper.DatabaseHelper.COLUMN_RESTAURANT_FK;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.FoodAdapter;
import com.example.tastewaveapp.databasehelper.DatabaseHelper;
import com.example.tastewaveapp.model.Food;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    private RecyclerView foodRecyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                // Navigate to Profile Activity
                startActivity(new Intent(RestaurantActivity.this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_orders) {
                // Navigate to Orders Activity
                startActivity(new Intent(RestaurantActivity.this, OrderActivity.class));
                return true;
            } else if (id == R.id.nav_payment) {
                // Navigate to Payment Activity
                startActivity(new Intent(RestaurantActivity.this, PaymentActivity.class));
                return true;
            } else if (id == R.id.nav_offers) {
                // Navigate to Offers Activity
                startActivity(new Intent(RestaurantActivity.this, OffersActivity.class));
                return true;
            }
            return false;
        });

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Get restaurant details from intent
        String restaurantName = getIntent().getStringExtra("restaurant_name");
        String restaurantDescription = getIntent().getStringExtra("restaurant_description");
        int restaurantImageResId = getIntent().getIntExtra("restaurant_image", 0);

        // Set up UI
        TextView nameTextView = findViewById(R.id.restaurant_name);
        TextView descriptionTextView = findViewById(R.id.restaurant_description);
        ImageView restaurantImageView = findViewById(R.id.restaurant_image);

        nameTextView.setText(restaurantName);
        descriptionTextView.setText(restaurantDescription);
        restaurantImageView.setImageResource(restaurantImageResId);

        // Set up RecyclerView for food list
        foodRecyclerView = findViewById(R.id.foodRecyclerView);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper.insertFood("MR.Kottu","good kottu", R.drawable.start,150);

        // Fetch foods from the database
        foodList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllFoodItemsByRestaurant();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID));
                String foodName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME));
                String foodDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_DESCRIPTION));
                int foodImageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_IMAGE));
                double foodPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PRICE));
                int restaurantId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_FK));

                foodList.add(new Food(id, foodName, foodDescription, foodImageResId, foodPrice, restaurantId));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Set up the adapter with Context, food list, and the OnFoodClickListener
        foodAdapter = new FoodAdapter(this, foodList, food -> {
            // Handle food item click
            Intent intent = new Intent(RestaurantActivity.this, FoodActivity.class);
            intent.putExtra("food_name", food.getName());
            intent.putExtra("food_description", food.getDescription());
            intent.putExtra("food_image_id", food.getImageResId());
            intent.putExtra("food_price", food.getPrice());
            startActivity(intent);
        });

        foodRecyclerView.setAdapter(foodAdapter);
    }
}
