package com.example.tastewaveapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.model.Order;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {

    private final Context context;
    private final List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        super(context, 0, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        }

        // Get the current order
        Order order = orders.get(position);

        // Initialize views
        TextView tvOrderId = convertView.findViewById(R.id.tv_order_id);
        TextView tvOrderStatus = convertView.findViewById(R.id.tv_order_status);
        TextView tvTotalPrice = convertView.findViewById(R.id.tv_total_price);
        TextView tvOrderDate = convertView.findViewById(R.id.tv_order_date);
        TextView tvDeliveryAddress = convertView.findViewById(R.id.tv_delivery_address);


        // Populate data
        tvOrderId.setText("Order ID: " + order.getOrderId());
        tvOrderStatus.setText("Status: " + order.getOrderStatus());
        tvTotalPrice.setText("Total: $" + String.format("%.2f", order.getTotalPrice()));
        tvOrderDate.setText("Date: " + order.getOrderDate());
        tvDeliveryAddress.setText("Address: " + order.getDeliveryAddress());


        // Change text color based on order status
        String status = order.getOrderStatus();
        if (status.equalsIgnoreCase("Completed")) {
            tvOrderStatus.setTextColor(context.getResources().getColor(R.color.green, null));
        } else if (status.equalsIgnoreCase("Pending")) {
            tvOrderStatus.setTextColor(context.getResources().getColor(R.color.orange, null));
        } else {
            tvOrderStatus.setTextColor(context.getResources().getColor(R.color.red, null));
        }

        return convertView;
    }
}
