package com.example.tastewaveapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.model.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantDescription.setText(restaurant.getDescription());
        holder.restaurantImage.setImageResource(restaurant.getImageResId());
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName, restaurantDescription;
        ImageView restaurantImage;

        public RestaurantViewHolder(View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantDescription = itemView.findViewById(R.id.restaurant_description);
            restaurantImage = itemView.findViewById(R.id.restaurant_image);
        }
    }
}

