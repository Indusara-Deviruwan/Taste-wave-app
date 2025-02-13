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
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaymentCartAdapter extends BaseAdapter {
    private Context context;
    private List<FoodCart> cartItems;

    public PaymentCartAdapter(Context context, List<FoodCart> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemPrice = convertView.findViewById(R.id.item_price);
        View removeButton = convertView.findViewById(R.id.item_delete_button); // Assuming the remove button exists
        ImageView itemImageView = convertView.findViewById(R.id.item_image);

        FoodCart cartItem = cartItems.get(position);
        itemName.setText(cartItem.getName());
        itemPrice.setText("$" + String.format("%.2f", cartItem.getTotalPrice()));
        Picasso.get().load(cartItem.getImageUrl()).into(itemImageView);

        // Hide the remove button
        if (removeButton != null) {
            removeButton.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void updateCartItems(List<FoodCart> updatedCart) {
        this.cartItems = updatedCart;
        notifyDataSetChanged();
    }
}
