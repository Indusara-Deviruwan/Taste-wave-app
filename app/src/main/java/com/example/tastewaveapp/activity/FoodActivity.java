package com.example.tastewaveapp.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastewaveapp.R;

public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get food details from intent
        String foodName = getIntent().getStringExtra("food_name");
        String foodDescription = getIntent().getStringExtra("food_description");
        int foodImageResId = getIntent().getIntExtra("food_image", R.drawable.start);
        double foodPrice  = getIntent().getDoubleExtra("food_price", 0);

        // Set up UI
        TextView nameTextView = findViewById(R.id.food_name);
        TextView descriptionTextView = findViewById(R.id.food_description);
        ImageView foodImageView = findViewById(R.id.food_image);
        TextView foodPriceTextView = findViewById(R.id.food_price);

        nameTextView.setText(foodName);
        descriptionTextView.setText(foodDescription);
        foodImageView.setImageResource(foodImageResId);
        //foodPriceTextView.setInt(foodPrice);
    }
}