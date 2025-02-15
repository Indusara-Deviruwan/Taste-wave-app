package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tastewaveapp.R;
import com.google.android.material.button.MaterialButton;

public class FoodActivity extends BaseActivity {

    private TextView quantityTextView;
    private int quantity = 0;
    private String foodPrice;
    private String restaurantId, restaurantName;
    private String foodId, foodName, foodDescription, foodImageResId;
    // Removed CartDatabaseHelper since we're not using add-to-cart functionality anymore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Setup Bottom Navigation and Toolbar
        setupBottomNavigation();
        setupToolbar("Food");

        // Get food details from intent
        restaurantId = getIntent().getStringExtra("restaurant_id");
        restaurantName = getIntent().getStringExtra("restaurant_name");

        foodId = getIntent().getStringExtra("food_id");
        foodName = getIntent().getStringExtra("food_name");
        foodDescription = getIntent().getStringExtra("food_description");
        foodImageResId = getIntent().getStringExtra("food_image");
        foodPrice = getIntent().getStringExtra("food_price");

        // Set up UI elements
        TextView nameTextView = findViewById(R.id.food_name);
        TextView descriptionTextView = findViewById(R.id.food_description);
        ImageView foodImageView = findViewById(R.id.food_image);
        TextView foodPriceTextView = findViewById(R.id.food_price);
        quantityTextView = findViewById(R.id.quantity_text);

        nameTextView.setText(foodName);
        descriptionTextView.setText(foodDescription);
        Glide.with(this).load(foodImageResId).into(foodImageView);
        foodPriceTextView.setText(foodPrice);

        // Set initial quantity
        quantityTextView.setText(String.valueOf(quantity));

        // Setup increase and decrease buttons
        MaterialButton buttonRemove = findViewById(R.id.button_remove);
        MaterialButton buttonAdd = findViewById(R.id.button_add);

        buttonRemove.setOnClickListener(v -> {
            if (quantity > 0) {
                quantity--;
                updateQuantity();
            }
        });

        buttonAdd.setOnClickListener(v -> {
            quantity++;
            updateQuantity();
        });

        // Setup Add Review Rating button (changed from Add to Cart)
        MaterialButton buttonAddReviewRating = findViewById(R.id.button_add_review_rating);
        buttonAddReviewRating.setOnClickListener(v -> AddReviewRating());
    }

    private void updateQuantity() {
        quantityTextView.setText(String.valueOf(quantity));
    }

    // This method launches the ReviewRatingActivity and passes food and restaurant details
    private void AddReviewRating() {
        Intent intent = new Intent(FoodActivity.this, ReviewRatingActivity.class);
        intent.putExtra("restaurant_id", restaurantId);
        intent.putExtra("restaurant_name", restaurantName);
        intent.putExtra("food_id", foodId);
        intent.putExtra("food_name", foodName);
        intent.putExtra("food_description", foodDescription);
        intent.putExtra("food_image", foodImageResId);
        intent.putExtra("food_price", foodPrice);
        startActivity(intent);
    }

    @Override
    protected String getToolbarTitle() {
        return "Foods";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return -1;
    }
}
