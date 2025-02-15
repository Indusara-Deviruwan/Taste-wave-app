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
import com.example.tastewaveapp.model.Restaurant;

import java.util.List;

public class RestaurantAdapter extends BaseAdapter {

    private final Context context;
    private final List<Restaurant> restaurantList;
    private final LayoutInflater inflater;

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
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

        Restaurant restaurant = restaurantList.get(position);

        // Load image with Glide
        Glide.with(context)
                .load(restaurant.getImageResId())
                .placeholder(R.drawable.splash_3)
                .error(R.drawable.splash_4)
                .into(holder.imageView);

        holder.nameTextView.setText(restaurant.getName());
        holder.descriptionTextView.setText(restaurant.getDescription());

        holder.favoriteButton.setOnClickListener(v ->
                Toast.makeText(context, "Favorite clicked for " + restaurant.getName(), Toast.LENGTH_SHORT).show()
        );

        return convertView;
    }

    // Update the existing list rather than replacing the reference
    public void updateRestaurants(List<Restaurant> newList) {
        restaurantList.clear();
        restaurantList.addAll(newList);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        ImageButton favoriteButton;
    }
}
