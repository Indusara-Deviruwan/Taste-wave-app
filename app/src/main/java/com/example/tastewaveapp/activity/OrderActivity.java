package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.OrderAdapter;
import com.example.tastewaveapp.databasehelper.OrderDatabaseHelper;
import com.example.tastewaveapp.model.Order;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {

    private ListView ordersListView;
    private MaterialButton filterAllOrdersButton, filterPendingOrdersButton, filterCompletedOrdersButton;
    private OrderDatabaseHelper dbHelper;
    private List<Order> orders;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize Views
        ordersListView = findViewById(R.id.orders_list_view);
        filterAllOrdersButton = findViewById(R.id.filter_all_orders);
        filterPendingOrdersButton = findViewById(R.id.filter_pending_orders);
        filterCompletedOrdersButton = findViewById(R.id.filter_completed_orders);

        // Setup Bottom Navigation
        setupBottomNavigation();

        // Toolbar setup
        setupToolbar("Orders");

        // Initialize database helper
        dbHelper = new OrderDatabaseHelper(this);

        // Set up order filter buttons
        setupOrderFilters();

        // Load and display orders
        loadOrders("All");
    }

    @Override
    protected String getToolbarTitle() {
        return "Orders";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return R.id.nav_orders;
    }

    private void setupOrderFilters() {
        filterAllOrdersButton.setOnClickListener(v -> loadOrders("All"));
        filterPendingOrdersButton.setOnClickListener(v -> loadOrders("Pending"));
        filterCompletedOrdersButton.setOnClickListener(v -> loadOrders("Completed"));
    }

    private void loadOrders(String filter) {
        orders = new ArrayList<>();
        for (int id = 1; id <= 100; id++) {
            Order order = dbHelper.getOrderById(id);
            if (order != null) {
                if (filter.equals("All") || order.getOrderStatus().equalsIgnoreCase(filter)) {
                    orders.add(order);
                }
            }
        }

        if (orders.isEmpty()) {
            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
        }

        adapter = new OrderAdapter(this, orders);
        ordersListView.setAdapter(adapter);

        ordersListView.setOnItemClickListener((parent, view, position, id) -> {
            Order selectedOrder = orders.get(position);
            Intent intent = new Intent(OrderActivity.this, OrderDetailsActivity.class);
            intent.putExtra("orderId", selectedOrder.getOrderId());
            intent.putExtra("foodItems", (Serializable) selectedOrder.getFoodItems());

            startActivity(intent);
        });
    }
}
