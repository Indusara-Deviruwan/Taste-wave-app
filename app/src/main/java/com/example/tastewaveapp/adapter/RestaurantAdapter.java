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
import com.example.tastewaveapp.model.Restaurants;

import java.util.List;

public class RestaurantAdapter extends BaseAdapter {

    private final Context context;
    private final List<Restaurants> restaurantList;
    private final LayoutInflater inflater;

    public RestaurantAdapter(Context context, List<Restaurants> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_restaurant, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.restaurant_image);
            holder.nameTextView = convertView.findViewById(R.id.restaurant_name);
            holder.descriptionTextView = convertView.findViewById(R.id.restaurant_description);
            holder.favoriteButton = convertView.findViewById(R.id.buttonFavorite);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current restaurant
        Restaurants restaurant = restaurantList.get(position);

        // Load image from URL using Glide
        Glide.with(context)
                .load(restaurant.getImageResId())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Failed to load image: ", e);
                        return false; // Allow Glide to handle the error
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .placeholder(R.drawable.splash_3) // Placeholder while loading
                .error(R.drawable.splash_4) // Error image if loading fails
                .into(holder.imageView);


        // Set name and description
        holder.nameTextView.setText(restaurant.getName());
        holder.descriptionTextView.setText(restaurant.getDescription());

        // Handle favorite button click
        holder.favoriteButton.setOnClickListener(v ->
                Toast.makeText(context, "Favorite clicked for " + restaurant.getName(), Toast.LENGTH_SHORT).show()
        );

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        ImageButton favoriteButton;
    }
}