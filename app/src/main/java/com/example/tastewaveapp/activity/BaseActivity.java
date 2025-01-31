package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tastewaveapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        setupBottomNavigation();
    }

    // Initialize Bottom Navigation
    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Highlight the current menu item
        int selectedItemId = getSelectedMenuItemId();
        if (selectedItemId != 0) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home && getSelectedMenuItemId() != R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish(); // Finish the current activity
                return true;
            } else if (id == R.id.nav_search && getSelectedMenuItemId() != R.id.nav_search) {
                startActivity(new Intent(this, PaymentActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_orders && getSelectedMenuItemId() != R.id.nav_orders) {
                startActivity(new Intent(this, OrderActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile && getSelectedMenuItemId() != R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    // Get selected menu item (override in each activity)
    protected abstract int getSelectedMenuItemId();
}
