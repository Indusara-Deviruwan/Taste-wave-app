package com.example.tastewaveapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.model.FoodCart;
import com.squareup.picasso.Picasso; // If using Picasso to load images

import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<FoodCart> cartItems;
    private LayoutInflater inflater;

    public CartAdapter(Context context, List<FoodCart> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cartItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_cart, parent, false);
        }

        FoodCart foodCart = cartItems.get(position);

        // Reference to UI components
        TextView nameTextView = convertView.findViewById(R.id.item_name);
        TextView quantityTextView = convertView.findViewById(R.id.item_quantity);
        TextView priceTextView = convertView.findViewById(R.id.item_price);
        ImageView itemImageView = convertView.findViewById(R.id.item_image);

        // Set data to the views
        nameTextView.setText(foodCart.getName());
        quantityTextView.setText("Qty: " + foodCart.getQuantity());
        priceTextView.setText("$" + String.format("%.2f", foodCart.getTotalPrice()));

        // Load image (you can use any image loading library like Picasso or Glide)
        Picasso.get().load(foodCart.getImageUrl()).into(itemImageView);

        return convertView;
    }
}
