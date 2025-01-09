package com.example.tastewaveapp.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.FoodAdapter;
import com.example.tastewaveapp.model.Food;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    private RecyclerView foodRecyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

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

        // Populate dynamic food list
        foodList = new ArrayList<>();
        foodList.add(new Food("Pizza Margherita", "Classic Italian pizza with tomatoes and mozzarella.", R.drawable.start));
        foodList.add(new Food("Sushi Platter", "A variety of fresh sushi rolls.", R.drawable.start));
        foodList.add(new Food("Cheeseburger", "Juicy beef burger with cheese, lettuce, and tomato.", R.drawable.start));

        foodAdapter = new FoodAdapter(foodList);
        foodRecyclerView.setAdapter(foodAdapter);
    }
}
