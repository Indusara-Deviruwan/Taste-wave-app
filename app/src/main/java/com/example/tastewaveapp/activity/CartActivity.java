package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.CartAdapter;
import com.example.tastewaveapp.databasehelper.CartDatabaseHelper;
import com.example.tastewaveapp.model.FoodCart;

import java.io.Serializable;
import java.util.List;

public class CartActivity extends BaseActivity {

    private CartDatabaseHelper cartDatabaseHelper;
    private ListView cartItemsListView;
    private TextView totalPriceTextView;
    private CartAdapter cartAdapter;
    private List<FoodCart> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setupToolbar("Cart");  // Set up toolbar
        setupBottomNavigation();

        cartDatabaseHelper = new CartDatabaseHelper(this);
        cartItemsListView = findViewById(R.id.cart_items_list_view);
        totalPriceTextView = findViewById(R.id.total_price);

        // Load cart items from database
        loadCartItems();

        // Set up adapter for ListView
        cartAdapter = new CartAdapter(this, cartItems);
        cartItemsListView.setAdapter(cartAdapter);

        // Calculate and display total price
        displayTotalPrice();

        // Handle promo code input (optional functionality)
        findViewById(R.id.apply_promo_code_button).setOnClickListener(v -> applyPromoCode());

        // Handle checkout action
        findViewById(R.id.checkout_button).setOnClickListener(v -> proceedToCheckout());
    }

    private void loadCartItems() {
        cartItems = cartDatabaseHelper.getAllCartItems(); // Retrieve all cart items
    }

    private void displayTotalPrice() {
        double totalPrice = 0.0;
        for (FoodCart item : cartItems) {
            totalPrice += item.getTotalPrice(); // Calculate total price
        }
        totalPriceTextView.setText("Total: $" + String.format("%.2f", totalPrice)); // Display total price
    }

    private void applyPromoCode() {
        // Logic for applying promo code (optional functionality)
    }

    private void proceedToCheckout() {
        // Create an intent to navigate to PaymentActivity
        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);

        // Pass the cart items to the PaymentActivity
        intent.putExtra("cartItems", (Serializable) cartItems);

        // Start PaymentActivity
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems(); // Reload cart items in case of any updates
        cartAdapter.notifyDataSetChanged(); // Refresh ListView
        displayTotalPrice(); // Update total price
    }

    @Override
    protected String getToolbarTitle() {
        return "Cart";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return R.id.nav_cart;
    }
}
