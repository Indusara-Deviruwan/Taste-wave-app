package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.FoodAdapter;
import com.example.tastewaveapp.model.Food;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BaseActivity {

    private ListView foodListView;
    private FoodAdapter foodAdapter;
    private List<String> foodNames;
    private FirebaseFirestore db;
    private String restaurantId;
    private List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup Bottom Navigation
        setupBottomNavigation();
        setupToolbar("Restaurant");

        // Get Firestore instance
        db = FirebaseFirestore.getInstance();

        // Get restaurant details from intent
        restaurantId = getIntent().getStringExtra("RESTAURANT_ID");
        String restaurantName = getIntent().getStringExtra("RESTAURANT_NAME");
        String restaurantDescription = getIntent().getStringExtra("RESTAURANT_DESCRIPTION");
        String restaurantImageResId = getIntent().getStringExtra("RESTAURANT_IMAGE");

        // Set up UI
        TextView nameTextView = findViewById(R.id.restaurant_name);
        TextView descriptionTextView = findViewById(R.id.restaurant_description);
        ImageView restaurantImageView = findViewById(R.id.restaurant_image);
        foodListView = findViewById(R.id.foodListView);

        nameTextView.setText(restaurantName);
        descriptionTextView.setText(restaurantDescription);
        Glide.with(this).load(restaurantImageResId).into(restaurantImageView);

        // Initialize list and adapter
        foodList = new ArrayList<>();
        foodNames = new ArrayList<>();
        foodAdapter = new FoodAdapter(this,foodList,restaurantId,restaurantName);
        foodListView.setAdapter(foodAdapter);

        // Load food items
        fetchFoodItems();

        // Set item click listener
        foodListView.setOnItemClickListener((parent, view, position, id) -> {
            Food selectedFood = foodList.get(position);
            Intent intent = new Intent(RestaurantActivity.this, FoodActivity.class);
            intent.putExtra("food_name", selectedFood.getName());
            intent.putExtra("food_description", selectedFood.getDescription());
            intent.putExtra("food_image", selectedFood.getImageResId());
            intent.putExtra("food_price", selectedFood.getPrice());
            startActivity(intent);
        });
    }

    @Override
    protected String getToolbarTitle() {
        return "Restaurant";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return -1;
    }

    private void fetchFoodItems() {
        db.collection("Foods")
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    foodList.clear();
                    foodNames.clear();

                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        String id = document.getId();
                        String name = document.getString("name");
                        String description = document.getString("description");
                        String imageResId = document.getString("imageResId");
                        String price = document.getString("price");

                        Food food = new Food(id, name, description, imageResId, price, restaurantId);
                        foodList.add(food);
                        foodNames.add(name);
                    }

                    foodAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("RestaurantActivity", "Error fetching food items", e);
                    Toast.makeText(this, "Failed to load food items", Toast.LENGTH_SHORT).show();
                });
    }
}