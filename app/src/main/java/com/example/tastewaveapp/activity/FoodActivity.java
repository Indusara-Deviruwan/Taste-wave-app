package com.example.tastewaveapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.CartDatabaseHelper;
import com.example.tastewaveapp.model.FoodCart;
import com.google.android.material.button.MaterialButton;

public class FoodActivity extends BaseActivity {

    private TextView quantityTextView;
    private int quantity = 0;
    private String foodPrice;
    private String foodName, foodDescription, foodImageResId;
    private CartDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        dbHelper = new CartDatabaseHelper(this);

        // Get food details from intent
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

        // Setup Add to Cart button
        MaterialButton buttonAddToCart = findViewById(R.id.button_add_to_cart);
        buttonAddToCart.setOnClickListener(v -> addToCart());
    }

    private void updateQuantity() {
        quantityTextView.setText(String.valueOf(quantity));
    }

    private void addToCart() {
        if (quantity > 0) {
            // Create FoodCart object to add to the database
            FoodCart foodCartItem = new FoodCart();
            foodCartItem.setName(foodName);
            foodCartItem.setDescription(foodDescription);

            String priceString = foodPrice;
            // Remove the dollar sign and trim whitespace
            String cleanPrice = priceString.replace("$", "").trim();
            // Convert to double
            double foodPrice = Double.parseDouble(cleanPrice);
            // Output: 50.00

            foodCartItem.setPrice(foodPrice);
            foodCartItem.setQuantity(quantity);
            foodCartItem.setImageUrl(foodImageResId);

            // Insert into SQLite database
            dbHelper.addToCart(foodCartItem);

            // Close the database
            dbHelper.close();

            // Show success toast message
            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
        } else {
            // Show error toast message if quantity is zero
            Toast.makeText(this, "Quantity must be greater than zero", Toast.LENGTH_SHORT).show();
        }
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
