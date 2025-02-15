package com.example.tastewaveapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.model.ReviewRating;
import java.util.List;

public class ReviewRatingAdapter extends ArrayAdapter<ReviewRating> {

    private Context context;
    private List<ReviewRating> reviewList;

    public ReviewRatingAdapter(Context context, List<ReviewRating> reviews) {
        super(context, R.layout.item_review_rating, reviews);
        this.context = context;
        this.reviewList = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_review_rating, parent, false);
        }

        ReviewRating review = reviewList.get(position);

        TextView restaurantName = convertView.findViewById(R.id.text_restaurant_name);
        TextView foodName = convertView.findViewById(R.id.text_food_name);
        TextView reviewText = convertView.findViewById(R.id.text_review);
        RatingBar ratingBar = convertView.findViewById(R.id.rating_bar);
        ImageView photo = convertView.findViewById(R.id.image_photo);

        restaurantName.setText(review.getRestaurantName());
        foodName.setText(review.getFoodName());
        reviewText.setText(review.getReviewText());
        ratingBar.setRating(review.getRating());

        if (review.getPhotoUri() != null && !review.getPhotoUri().isEmpty()) {
            Glide.with(context).load(review.getPhotoUri()).into(photo);
        } else {
            photo.setImageResource(R.drawable.spalsh_1);
        }

        return convertView;
    }
}
