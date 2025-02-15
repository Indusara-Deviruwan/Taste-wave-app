package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.ReviewRatingDatabaseHelper;
import com.example.tastewaveapp.model.ReviewRating;
import com.google.android.material.button.MaterialButton;

public class ReviewRatingActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;  // Added camera request code

    private TextView foodNameTextView, foodDescriptionTextView;
    private ImageView foodImageView, uploadedPhotoImageView;
    private EditText editTextReview;
    private RatingBar ratingBar;
    private MaterialButton buttonUploadPhoto, buttonUploadCamera, buttonSubmitReview; // Added camera button

    private String restaurantId, restaurantName, foodId, foodName, foodDescription, foodImageResId, foodPrice;
    private ReviewRatingDatabaseHelper reviewRatingDatabaseHelper;
    private String selectedPhotoUriString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rating);

        setupBottomNavigation();
        setupToolbar("Review & Rating");

        foodNameTextView = findViewById(R.id.food_name);
        foodDescriptionTextView = findViewById(R.id.food_description);
        foodImageView = findViewById(R.id.food_image);
        ratingBar = findViewById(R.id.rating_bar);
        buttonUploadPhoto = findViewById(R.id.button_upload_photo);
        buttonUploadCamera = findViewById(R.id.button_upload_camera);  // Camera button
        uploadedPhotoImageView = findViewById(R.id.uploaded_photo);
        editTextReview = findViewById(R.id.editText_review);
        buttonSubmitReview = findViewById(R.id.button_submit_review);

        // Retrieve extras
        restaurantId = getIntent().getStringExtra("restaurant_id");
        restaurantName = getIntent().getStringExtra("restaurant_name");
        foodId = getIntent().getStringExtra("food_id");
        foodName = getIntent().getStringExtra("food_name");
        foodDescription = getIntent().getStringExtra("food_description");
        foodImageResId = getIntent().getStringExtra("food_image");
        foodPrice = getIntent().getStringExtra("food_price");

        if (foodImageResId != null && !foodImageResId.isEmpty()) {
            Glide.with(this).load(foodImageResId).into(foodImageView);
        }
        if (foodName != null) foodNameTextView.setText(foodName);
        if (foodDescription != null) foodDescriptionTextView.setText(foodDescription);

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                Toast.makeText(ReviewRatingActivity.this, "Rating: " + rating, Toast.LENGTH_SHORT).show()
        );

        buttonUploadPhoto.setOnClickListener(v -> openImageChooser());
        buttonUploadCamera.setOnClickListener(v -> openCamera());  // Camera upload handler

        reviewRatingDatabaseHelper = new ReviewRatingDatabaseHelper(this);
        buttonSubmitReview.setOnClickListener(v -> submitReviewRating());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {  // New method to open the camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private void submitReviewRating() {
        float rating = ratingBar.getRating();
        String reviewText = editTextReview.getText().toString().trim();
        if (reviewText.isEmpty()) {
            Toast.makeText(this, "Please enter your review", Toast.LENGTH_SHORT).show();
            return;
        }
        ReviewRating reviewRating = new ReviewRating();
        reviewRating.setRestaurantId(restaurantId);
        reviewRating.setRestaurantName(restaurantName);
        reviewRating.setFoodId(foodId);
        reviewRating.setFoodName(foodName);
        reviewRating.setRating(rating);
        reviewRating.setReviewText(reviewText);
        reviewRating.setPhotoUri(selectedPhotoUriString);

        boolean isInserted = reviewRatingDatabaseHelper.addReviewRating(reviewRating) > 0;
        Toast.makeText(this, isInserted ? "Review submitted successfully" : "Failed to submit review", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String getToolbarTitle() {
        return "Review & Rating";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri imageUri = null;
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                imageUri = data.getData();
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), (android.graphics.Bitmap) extras.get("data"), null, null));
                }
            }
            if (imageUri != null) {
                selectedPhotoUriString = imageUri.toString();
                Glide.with(this).load(imageUri).into(uploadedPhotoImageView);
                Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
