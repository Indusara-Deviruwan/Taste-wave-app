package com.example.tastewaveapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.CartDatabaseHelper;
import com.example.tastewaveapp.model.Food;
import com.example.tastewaveapp.model.FoodCart;

import java.util.List;

public class FoodAdapter extends BaseAdapter {

    private final Context context;
    private final List<Food> foodList;
    private final LayoutInflater inflater;
    private final String restaurantId;
    private final String restaurantName;

    public FoodAdapter(Context context, List<Food> foodList, String restaurantId , String restaurantName) {
        this.context = context;
        this.foodList = foodList;
        this.inflater = LayoutInflater.from(context);
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_food, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.food_image);
            holder.nameTextView = convertView.findViewById(R.id.food_name);
            holder.descriptionTextView = convertView.findViewById(R.id.food_description);
            holder.priceTextView = convertView.findViewById(R.id.food_price);
            holder.favoriteButton = convertView.findViewById(R.id.buttonFavorite);
            holder.increaseButton = convertView.findViewById(R.id.btn_increase);
            holder.decreaseButton = convertView.findViewById(R.id.btn_decrease);
            holder.cartButton = convertView.findViewById(R.id.buttonAddToCart);
            holder.quantityTextView = convertView.findViewById(R.id.tv_quantity);
            holder.quantity = 0; // Default quantity is 0

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current food item
        Food food = foodList.get(position);

        // Load image from URL using Glide
        Glide.with(context)
                .load(food.getImageResId())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Failed to load image: ", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .placeholder(R.drawable.start)
                .error(R.drawable.splash_4)
                .into(holder.imageView);

        // Set name, description, and price
        holder.nameTextView.setText(food.getName());
        holder.descriptionTextView.setText(food.getDescription());
        holder.priceTextView.setText(food.getPrice());

        // Update quantity display
        holder.quantityTextView.setText(String.valueOf(holder.quantity));

        // Handle increase button click
        holder.increaseButton.setOnClickListener(v -> {
            holder.quantity++;
            holder.quantityTextView.setText(String.valueOf(holder.quantity));
        });

        // Handle decrease button click
        holder.decreaseButton.setOnClickListener(v -> {
            if (holder.quantity > 0) {
                holder.quantity--;
                holder.quantityTextView.setText(String.valueOf(holder.quantity));
            }
        });

        // Handle favorite button click
        holder.favoriteButton.setOnClickListener(v ->
                Toast.makeText(context, "Favorite clicked for " + food.getName(), Toast.LENGTH_SHORT).show()
        );

        // Handle cart button click
        holder.cartButton.setOnClickListener(v -> {
            if (holder.quantity > 0) {
                CartDatabaseHelper dbHelper = new CartDatabaseHelper(context);

                FoodCart foodCart = new FoodCart();

                foodCart.setName(food.getName());
                foodCart.setDescription(food.getDescription());
                foodCart.setRestaurantId(restaurantId);
                foodCart.setRestaurantName(restaurantName);

                String priceString = food.getPrice();
                String cleanPrice = priceString.replace("$", "").trim();
                double price = Double.parseDouble(cleanPrice);

                foodCart.setPrice(price);
                foodCart.setQuantity(holder.quantity);
                foodCart.setImageUrl(food.getImageResId());

                dbHelper.addToCart(foodCart);
                dbHelper.close();

                Toast.makeText(context, "Added " + holder.quantity + " " + food.getName() + " to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please select at least one item", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageButton favoriteButton, increaseButton, decreaseButton, cartButton;
        int quantity;
    }
}
