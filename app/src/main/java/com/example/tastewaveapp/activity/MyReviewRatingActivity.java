package com.example.tastewaveapp.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.ReviewRatingAdapter;
import com.example.tastewaveapp.databasehelper.ReviewRatingDatabaseHelper;
import com.example.tastewaveapp.model.ReviewRating;
import java.util.List;

public class MyReviewRatingActivity extends AppCompatActivity {

    private ListView listView;
    private ReviewRatingAdapter adapter;
    private ReviewRatingDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review_rating);

        listView = findViewById(R.id.listView_reviews);
        dbHelper = new ReviewRatingDatabaseHelper(this);

        loadReviews();
    }

    private void loadReviews() {
        List<ReviewRating> reviewList = dbHelper.getAllReviewRatings();

        if (reviewList.isEmpty()) {
            Toast.makeText(this, "No reviews available.", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new ReviewRatingAdapter(this, reviewList);
            listView.setAdapter(adapter);
        }
    }
}
