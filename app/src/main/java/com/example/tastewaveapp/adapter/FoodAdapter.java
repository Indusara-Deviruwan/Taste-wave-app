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
import com.example.tastewaveapp.model.Food;

import java.util.List;

public class FoodAdapter extends BaseAdapter {

    private final Context context;
    private final List<Food> foodList;
    private final LayoutInflater inflater;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
        this.inflater = LayoutInflater.from(context);
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
                        return false; // Allow Glide to handle the error
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .placeholder(R.drawable.start) // Placeholder while loading
                .error(R.drawable.splash_4) // Error image if loading fails
                .into(holder.imageView);

        // Set name and description
        holder.nameTextView.setText(food.getName());
        holder.descriptionTextView.setText(food.getDescription());
        holder.priceTextView.setText(food.getPrice());


        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;

    }
}
