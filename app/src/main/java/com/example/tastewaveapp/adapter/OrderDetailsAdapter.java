package com.example.tastewaveapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.model.FoodCart;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailsAdapter extends android.widget.BaseAdapter {
    private Context context;
    private List<FoodCart> cartItems;
    private LayoutInflater inflater;
    private FoodItemListener  listener;

    public interface FoodItemListener {
        void removeItemFromCart(int id);
    }

    public OrderDetailsAdapter(Context context, List<FoodCart> cartItems, FoodItemListener  listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
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
            convertView = inflater.inflate(R.layout.order_item, parent, false);
        }

        FoodCart foodCart = cartItems.get(position);

        TextView nameTextView = convertView.findViewById(R.id.item_name);
        TextView quantityTextView = convertView.findViewById(R.id.item_quantity);
        TextView priceTextView = convertView.findViewById(R.id.item_price);
        ImageView itemImageView = convertView.findViewById(R.id.item_image);
        ImageView deleteButton = convertView.findViewById(R.id.item_delete_button);

        nameTextView.setText(foodCart.getName());
        quantityTextView.setText("Qty: " + foodCart.getQuantity());
        priceTextView.setText("$" + String.format("%.2f", foodCart.getTotalPrice()));

        String imageUrl = foodCart.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.spalsh_1)
                    .error(R.drawable.splash_3)
                    .into(itemImageView);
        } else {
            itemImageView.setImageResource(R.drawable.splash_2);
        }

        deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.removeItemFromCart(foodCart.getId());
                Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public void updateCartItems(List<FoodCart> updatedCartItems) {
        this.cartItems = updatedCartItems;
        notifyDataSetChanged();
    }
}
