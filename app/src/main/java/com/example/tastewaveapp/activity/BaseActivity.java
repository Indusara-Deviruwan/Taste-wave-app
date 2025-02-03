package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.tastewaveapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        setupToolbar();  // Set up toolbar
        setupBottomNavigation();  // Set up bottom nav
    }

    // Initialize Toolbar
    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide default title

        /*    // Set dynamic title if needed
            TextView titleText = findViewById(R.id.toolbar_title);
            if (titleText != null) {
                titleText.setText(getToolbarTitle());  // Call abstract method to get title
            } */
        }
    }

    // Child activities should override this to set toolbar title dynamically
    protected abstract String getToolbarTitle();

    // Initialize Bottom Navigation
    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        int selectedItemId = getIntent().getIntExtra("selected_menu_item", getSelectedMenuItemId());

        if (selectedItemId != -1) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        } else {
            bottomNavigationView.getMenu().setGroupCheckable(0, true, false);
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                bottomNavigationView.getMenu().getItem(i).setChecked(false);
            }
            bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                navigateTo(HomeActivity.class, id);
                return true;
            } else if (id == R.id.nav_cart) {
                navigateTo(CartActivity.class, id);
                return true;
            } else if (id == R.id.nav_orders) {
                navigateTo(OrderActivity.class, id);
                return true;
            } else if (id == R.id.nav_profile) {
                navigateTo(ProfileActivity.class, id);
                return true;
            }
            return false;
        });
    }

    private void navigateTo(Class<?> targetActivity, int selectedItemId) {
        Intent intent = new Intent(this, targetActivity);
        intent.putExtra("selected_menu_item", selectedItemId);
        startActivity(intent);
    }

    protected abstract int getSelectedMenuItemId();
}
