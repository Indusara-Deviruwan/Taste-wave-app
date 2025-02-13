package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.OrderDatabaseHelper;
import com.example.tastewaveapp.model.FoodCart;
import com.example.tastewaveapp.model.Order;

import java.util.List;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView orderDetailsTextView;
    private TextView totalPriceTextView;
    private Button confirmOrderButton;
    private OrderDatabaseHelper orderDatabaseHelper;
    private List<FoodCart> cartItems;
    private double totalPrice;
    private String deliveryAddress, paymentDetails;
    private String paymentMethod, cardNumber, expiryDate, cvv;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        orderDetailsTextView = findViewById(R.id.orderDetailsTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);

        orderDatabaseHelper = new OrderDatabaseHelper(this);

        Intent intent = getIntent();
        cartItems = (List<FoodCart>) intent.getSerializableExtra("cartItems");
        totalPrice = intent.getDoubleExtra("totalPrice", 0.0);
        deliveryAddress = intent.getStringExtra("deliveryAddress");
        paymentDetails = intent.getStringExtra("paymentDetails");
        cardNumber = intent.getStringExtra("cardNumber");
        expiryDate = intent.getStringExtra("expiryDate");
        cvv = intent.getStringExtra("cvv");
        userId = intent.getIntExtra("userId", -1);

        displayOrderDetails();

        confirmOrderButton.setOnClickListener(v -> confirmOrder());
    }

    private void displayOrderDetails() {
        StringBuilder foodItems = new StringBuilder();
        for (FoodCart item : cartItems) {
            foodItems.append(item.getName())
                    .append(" (Qty: ").append(item.getQuantity())
                    .append(")\n");
        }

        // Ensure all delivery details are properly displayed

        String residenceInfo = ( deliveryAddress != null && ! deliveryAddress.isEmpty()) ?  deliveryAddress : "Not provided";

        String paymentInfo = ( paymentDetails != null && ! paymentDetails.isEmpty()) ?  paymentDetails : "Not provided";
        orderDetailsTextView.setText(
                        "\nDelivery Details: " + residenceInfo +
                        "\n\nPayment Details:\n" + paymentInfo +
                        "\n\nFood Items:\n" + foodItems.toString()
        );

        totalPriceTextView.setText("Total: $" + String.format("%.2f", totalPrice));
    }

    private void confirmOrder() {
        // Ensure all required details are present
        if (deliveryAddress == null) {
            Toast.makeText(OrderConfirmationActivity.this, "Please fill all delivery details", Toast.LENGTH_SHORT).show();
            return;
        }

        Order newOrder = new Order();
        newOrder.setUserId(userId);
        newOrder.setFoodItems(cartItems);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setOrderStatus("Pending");
        newOrder.setOrderDate("2025-02-12");
        newOrder.setDeliveryAddress(deliveryAddress); // Combine delivery address details

        boolean isOrderInserted = orderDatabaseHelper.insertOrder(newOrder);

        if (isOrderInserted) {
            Intent homeIntent = new Intent(OrderConfirmationActivity.this, OrderActivity.class);
            Toast.makeText(OrderConfirmationActivity.this, "Order Placed successfully", Toast.LENGTH_SHORT).show();
            startActivity(homeIntent);
            finish();
        } else {
            Toast.makeText(OrderConfirmationActivity.this, "Order failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
