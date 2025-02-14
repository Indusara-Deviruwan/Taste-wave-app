package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.PaymentCartAdapter;
import com.example.tastewaveapp.databasehelper.CartDatabaseHelper;
import com.example.tastewaveapp.model.FoodCart;

import java.util.List;

public class PaymentActivity extends BaseActivity {

    private ListView cartListView;
    private PaymentCartAdapter paymentCartAdapter;
    private TextView totalAmountTextView;
    private TextView selectedPaymentMethodTextView;
    private Button addPaymentMethodButton;
    private Button proceedToConfirmationButton;
    private Button addDeliveryDetailsButton;

    private String selectedPaymentMethod = "None";
    private double totalPrice;
    private String deliveryAddress = "", paymentDetails;
    private int userId;
    private List<FoodCart> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        setupToolbar("Payment");
        setupBottomNavigation();

        cartListView = findViewById(R.id.payment_methods_list_view);
        totalAmountTextView = findViewById(R.id.total_amount);
        selectedPaymentMethodTextView = findViewById(R.id.selected_payment_method);
        addPaymentMethodButton = findViewById(R.id.payment_method_button);
        proceedToConfirmationButton = findViewById(R.id.pay_now_button);
        addDeliveryDetailsButton = findViewById(R.id.add_delivery_details_button);

        // Retrieve cart items from the database
        CartDatabaseHelper dbHelper = new CartDatabaseHelper(this);
        cartItems = dbHelper.getAllCartItems();
        totalPrice = dbHelper.getCartTotalPrice();

        paymentCartAdapter = new PaymentCartAdapter(this, cartItems);
        cartListView.setAdapter(paymentCartAdapter);

        displayTotalPrice();

        addPaymentMethodButton.setOnClickListener(v -> showAddPaymentMethodDialog());
        proceedToConfirmationButton.setOnClickListener(v -> proceedToOrderConfirmation());
        addDeliveryDetailsButton.setOnClickListener(v -> showAddDeliveryDetailsDialog());
    }

    private void displayTotalPrice() {
        totalAmountTextView.setText("Total Amount: $" + String.format("%.2f", totalPrice));
        selectedPaymentMethodTextView.setText("Selected Payment Method: " + selectedPaymentMethod);
    }

    private void showAddPaymentMethodDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_payment_method, null);

        EditText cardNumberEditText = dialogView.findViewById(R.id.payment_method_number);
        EditText expiryDateEditText = dialogView.findViewById(R.id.payment_method_expiry);
        EditText cvvEditText = dialogView.findViewById(R.id.payment_method_cvv);
        EditText paymentMethodEditText = dialogView.findViewById(R.id.payment_method_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setView(dialogView)
                .setTitle("Add Payment Method")
                .setPositiveButton("Save", (dialog, which) -> {
                    String paymentMethod = paymentMethodEditText.getText().toString().trim();
                    String cardNumber = cardNumberEditText.getText().toString().trim();
                    String expiryDate = expiryDateEditText.getText().toString().trim();
                    String cvv = cvvEditText.getText().toString().trim();

                    if (paymentMethod.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                        Toast.makeText(PaymentActivity.this, "Please fill all payment details", Toast.LENGTH_SHORT).show();
                    } else {
                        paymentDetails = "Payment Method: " + paymentMethod + "\nCard Number: " + cardNumber + "\nExpiry Date: " + expiryDate + "\nCVV: " + cvv;
                        Toast.makeText(this, "Payment details saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showAddDeliveryDetailsDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_delivery_details, null);

        EditText cityEditText = dialogView.findViewById(R.id.city_edit_text);
        EditText postalCodeEditText = dialogView.findViewById(R.id.postal_code_edit_text);
        EditText residenceAddressEditText = dialogView.findViewById(R.id.residence_address_edit_text);
        Spinner pickingTypeSpinner = dialogView.findViewById(R.id.picking_type_spinner);
        Spinner priorityTypeSpinner = dialogView.findViewById(R.id.priority_type_spinner);
        EditText deliveryTimeEditText = dialogView.findViewById(R.id.delivery_time_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setView(dialogView)
                .setTitle("Add Delivery Details")
                .setPositiveButton("Save", (dialog, which) -> {
                    String city = cityEditText.getText().toString().trim();
                    String postalCode = postalCodeEditText.getText().toString().trim();
                    String residenceAddress = residenceAddressEditText.getText().toString().trim();
                    String pickingType = pickingTypeSpinner.getSelectedItem().toString();
                    String priorityType = priorityTypeSpinner.getSelectedItem().toString();
                    String deliveryTime = deliveryTimeEditText.getText().toString().trim();

                    if (city.isEmpty() || postalCode.isEmpty() || residenceAddress.isEmpty() || deliveryTime.isEmpty()) {
                        Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                    } else {
                        deliveryAddress = "City: " + city + "\nPostal Code: " + postalCode + "\nAddress: " + residenceAddress + "\nPicking Type: " + pickingType + "\nPriority Type: " + priorityType + "\nDelivery Time: " + deliveryTime;
                        Toast.makeText(this, "Delivery details saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void proceedToOrderConfirmation() {
        if (deliveryAddress.isEmpty()) {
            Toast.makeText(this, "Please add delivery details", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent confirmationIntent = new Intent(PaymentActivity.this, OrderConfirmationActivity.class);
        confirmationIntent.putExtra("cartItems", (java.io.Serializable) cartItems);
        confirmationIntent.putExtra("totalPrice", totalPrice);
        confirmationIntent.putExtra("deliveryAddress", deliveryAddress);
        confirmationIntent.putExtra("userId", userId);
        confirmationIntent.putExtra("paymentDetails", paymentDetails);
        startActivity(confirmationIntent);
        finish();
    }

    @Override
    protected String getToolbarTitle() {
        return "Payments";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return -1;
    }
}
