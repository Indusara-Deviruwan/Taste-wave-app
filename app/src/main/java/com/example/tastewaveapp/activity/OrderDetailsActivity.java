package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.OrderDatabaseHelper;
import com.example.tastewaveapp.model.FoodCart;
import com.example.tastewaveapp.model.Order;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView orderIdTextView, userIdTextView, totalPriceTextView, statusTextView, dateTextView, addressTextView, foodItemsTextView;
    private Button trackButton;
    private int orderId; // from SQLite

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
        trackButton = findViewById(R.id.trackingButton);

        // Get order details from intent (orderId comes from SQLite)
        orderId = getIntent().getIntExtra("orderId", -1);
        List<FoodCart> foodItems = (List<FoodCart>) getIntent().getSerializableExtra("foodItems");

        if (orderId != -1) {
            loadOrderDetails(orderId, foodItems);
        } else {
            foodItemsTextView.setText("Invalid order ID.");
        }

        // Use the Firestore order document to fetch all restaurant locations
        trackButton.setOnClickListener(v -> {
            Log.d("OrderDetailsActivity", "Tracking button clicked");
            if (orderId != -1) {
                Log.d("OrderDetailsActivity", "Fetching locations for order ID: " + orderId);
                // Convert orderId to String as Firestore document IDs are strings.
                fetchRestaurantLocationsForOrder(String.valueOf(orderId));
            } else {
                Log.e("OrderDetailsActivity", "Invalid order ID for tracking.");
            }
        });
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

    /**
     * Fetches the order document from Firestore, extracts the list of food items,
     * and then retrieves all restaurant locations from the "Restaurants" collection.
     *
     * @param orderId The order document ID as a String.
     */
    private void fetchRestaurantLocationsForOrder(String orderId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the foodItems array from the order document.
                        List<Map<String, Object>> foodItems =
                                (List<Map<String, Object>>) documentSnapshot.get("foodItems");

                        if (foodItems != null && !foodItems.isEmpty()) {
                            // Extract restaurant IDs from the foodItems list.
                            List<String> restaurantIds = new ArrayList<>();
                            for (Map<String, Object> foodItem : foodItems) {
                                if (foodItem.containsKey("restaurantId")) {
                                    String restId = foodItem.get("restaurantId").toString();
                                    restaurantIds.add(restId);
                                }
                            }
                            // Now fetch the location for each restaurantId.
                            fetchRestaurantLocations(restaurantIds);
                        } else {
                            Log.e("OrderDetailsActivity", "No food items found in the order.");
                        }
                    } else {
                        Log.e("OrderDetailsActivity", "Order not found for orderId: " + orderId);
                    }
                })
                .addOnFailureListener(e -> Log.e("OrderDetailsActivity", "Failed to fetch order.", e));
    }

    /**
     * Fetches the GeoPoint locations for the given list of restaurant IDs.
     *
     * @param restaurantIds A list of restaurant IDs (as Strings).
     */
    private void fetchRestaurantLocations(List<String> restaurantIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<GeoPoint> locations = new ArrayList<>();
        final int totalRestaurants = restaurantIds.size();
        final AtomicInteger counter = new AtomicInteger(0);

        for (String restaurantId : restaurantIds) {
            db.collection("Restaurants")
                    .document(restaurantId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            GeoPoint location = documentSnapshot.getGeoPoint("location");
                            if (location != null) {
                                locations.add(location);
                            } else {
                                Log.e("OrderDetailsActivity", "No location found for restaurant: " + restaurantId);
                            }
                        } else {
                            Log.e("OrderDetailsActivity", "Restaurant not found: " + restaurantId);
                        }
                        // Once all fetches complete, navigate to TrackingActivity.
                        if (counter.incrementAndGet() == totalRestaurants) {
                            navigateToTracking(locations);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("OrderDetailsActivity", "Failed to fetch location for restaurant id: " + restaurantId, e);
                        if (counter.incrementAndGet() == totalRestaurants) {
                            navigateToTracking(locations);
                        }
                    });
        }
    }

    /**
     * Converts the list of GeoPoints to an ArrayList of double arrays and navigates to TrackingActivity.
     *
     * @param locations List of GeoPoints to be passed.
     */
    private void navigateToTracking(List<GeoPoint> locations) {
        ArrayList<double[]> locationList = new ArrayList<>();
        for (GeoPoint gp : locations) {
            locationList.add(new double[]{gp.getLatitude(), gp.getLongitude()});
        }
        Intent intent = new Intent(OrderDetailsActivity.this, TrackingActivity.class);
        intent.putExtra("restaurantLocations", locationList);
        startActivity(intent);
    }
}
