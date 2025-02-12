package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.CartAdapter;
import com.example.tastewaveapp.model.FoodCart;

import java.util.List;

public class PaymentActivity extends BaseActivity {

    private ListView paymentMethodsListView;
    private TextView selectedPaymentMethodTextView;
    private TextView totalAmountTextView;
    private CartAdapter cartAdapter;
    private List<FoodCart> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        // Set padding for system bars (status and navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar("Payment");  // Set up toolbar
        setupBottomNavigation();

        // Initialize UI components
        paymentMethodsListView = findViewById(R.id.payment_methods_list_view);
        selectedPaymentMethodTextView = findViewById(R.id.selected_payment_method);
        totalAmountTextView = findViewById(R.id.total_amount);

        // Retrieve the cart items passed from CartActivity
        Intent intent = getIntent();
        cartItems = (List<FoodCart>) intent.getSerializableExtra("cartItems");

        // Set up adapter for ListView to display cart items
        cartAdapter = new CartAdapter(this, cartItems);
        paymentMethodsListView.setAdapter(cartAdapter);

        // Calculate and display total price
        displayTotalPrice();
    }

    // Display total price based on cart items
    private void displayTotalPrice() {
        double totalPrice = 0.0;
        for (FoodCart item : cartItems) {
            totalPrice += item.getTotalPrice(); // Calculate total price
        }
        totalAmountTextView.setText("Total Amount: $" + String.format("%.2f", totalPrice)); // Display total price
    }

    @Override
    protected String getToolbarTitle() {
        return "Payments";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return 0;
    }
}
