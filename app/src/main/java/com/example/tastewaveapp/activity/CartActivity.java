package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.CartAdapter;
import com.example.tastewaveapp.databasehelper.CartDatabaseHelper;
import com.example.tastewaveapp.model.FoodCart;

import java.io.Serializable;
import java.util.List;

public class CartActivity extends BaseActivity implements CartAdapter.CartItemListener {

    private CartDatabaseHelper cartDatabaseHelper;
    private ListView cartItemsListView;
    private TextView totalPriceTextView;
    private CartAdapter cartAdapter;
    private List<FoodCart> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setupToolbar("Cart");
        setupBottomNavigation();

        cartDatabaseHelper = new CartDatabaseHelper(this);
        cartItemsListView = findViewById(R.id.cart_items_list_view);
        totalPriceTextView = findViewById(R.id.total_price);

        loadCartItems();

        cartAdapter = new CartAdapter(this, cartItems, this);  // Pass listener
        cartItemsListView.setAdapter(cartAdapter);

        displayTotalPrice();

        findViewById(R.id.apply_promo_code_button).setOnClickListener(v -> applyPromoCode());
        findViewById(R.id.checkout_button).setOnClickListener(v -> proceedToCheckout());
    }

    private void loadCartItems() {
        cartItems = cartDatabaseHelper.getAllCartItems();
    }

    private void displayTotalPrice() {
        double totalPrice = 0.0;
        for (FoodCart item : cartItems) {
            totalPrice += item.getTotalPrice();
        }
        totalPriceTextView.setText("Total: $" + String.format("%.2f", totalPrice));
    }

    private void applyPromoCode() {
        // Logic for applying promo code
    }

    private void proceedToCheckout() {
        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
        intent.putExtra("cartItems", (Serializable) cartItems);
        startActivity(intent);
    }

    public void removeItemFromCart(int id) {
        cartDatabaseHelper.removeItemFromCart(id);
        loadCartItems();
        cartAdapter.updateCartItems(cartItems);
        displayTotalPrice();
        Toast.makeText(this, "Item removed from cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
        cartAdapter.updateCartItems(cartItems);
        displayTotalPrice();
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
