package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastewaveapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                // Navigate to Profile Activity
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_orders) {
                // Navigate to Orders Activity
                startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
                return true;
            } else if (id == R.id.nav_payment) {
                // Navigate to Payment Activity
                startActivity(new Intent(ProfileActivity.this, PaymentActivity.class));
                return true;
            } else if (id == R.id.nav_offers) {
                // Navigate to Offers Activity
                startActivity(new Intent(ProfileActivity.this, OffersActivity.class));
                return true;
            }
            return false;
        });

    }
}