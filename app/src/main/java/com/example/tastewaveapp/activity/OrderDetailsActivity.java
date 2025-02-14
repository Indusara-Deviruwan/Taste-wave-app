package com.example.tastewaveapp.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.OrderDatabaseHelper;
import com.example.tastewaveapp.model.FoodCart;
import com.example.tastewaveapp.model.Order;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView orderIdTextView, userIdTextView, totalPriceTextView, statusTextView, dateTextView, addressTextView, foodItemsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Initialize views
        orderIdTextView = findViewById(R.id.order_id);
        userIdTextView = findViewById(R.id.order_user_id);
        totalPriceTextView = findViewById(R.id.order_total_price);
        statusTextView = findViewById(R.id.order_status);
        dateTextView = findViewById(R.id.order_date);
        addressTextView = findViewById(R.id.order_delivery_address);
        foodItemsTextView = findViewById(R.id.food_items_text);

        // Get order details from intent
        int orderId = getIntent().getIntExtra("orderId", -1);
        List<FoodCart> foodItems = (List<FoodCart>) getIntent().getSerializableExtra("foodItems");


        if (orderId != -1) {
            loadOrderDetails(orderId, foodItems);
        } else {
            foodItemsTextView.setText("Invalid order ID.");
        }
    }

    private void loadOrderDetails(int orderId, List<FoodCart> foodItems) {
        OrderDatabaseHelper dbHelper = new OrderDatabaseHelper(this);
        Order order = dbHelper.getOrderById(orderId);

        if (order != null) {
            // Set order details
            orderIdTextView.setText("Order ID: " + order.getOrderId());
            userIdTextView.setText("User ID: " + order.getUserId());
            totalPriceTextView.setText("Total Price: $" + String.format("%.2f", order.getTotalPrice()));
            statusTextView.setText("Status: " + order.getOrderStatus());
            dateTextView.setText("Date: " + order.getOrderDate());
            addressTextView.setText("Address: " + order.getDeliveryAddress());

            // Display food items passed from the intent
            if (foodItems == null || foodItems.isEmpty()) {
                foodItemsTextView.setText("No food items available.");
                Log.e("OrderDetailsActivity", "Food items list is empty");
            } else {
                StringBuilder foodItemsString = new StringBuilder();
                for (FoodCart foodCart : foodItems) {
                    // Assuming `getFoodName()` returns the food item name, `getQuantity()` returns the quantity,
                    // and `getRestaurantName()` returns the restaurant name.
                    foodItemsString.append(foodCart.getFoodName())
                            .append(" - Quantity: ")
                            .append(foodCart.getFoodQuantity())
                            .append(" - Restaurant: ")
                            .append(foodCart.getFoodRestaurantName())
                            .append("\n");
                }
                foodItemsTextView.setText(foodItemsString.toString());
            }
        } else {
            foodItemsTextView.setText("Order details not found.");
            Log.e("OrderDetailsActivity", "Failed to retrieve order");
        }
    }


}
