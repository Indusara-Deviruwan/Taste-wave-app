package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastewaveapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends BaseActivity {

    private ImageView profileImage;
    private EditText nameField, emailField, phoneField;
    private Button saveButton, changePasswordButton, logoutButton, editPictureButton;


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

        // Setup Bottom Navigation
        setupBottomNavigation();
        //tool bar
        setupToolbar("Profile");

        profileImage = findViewById(R.id.profile_image);
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        phoneField = findViewById(R.id.phone_field);
        saveButton = findViewById(R.id.save_button);
        editPictureButton = findViewById(R.id.edit_picture_button);

    }

    @Override
    protected String getToolbarTitle() {
        return "Profile";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return R.id.nav_profile; // Highlight "Home" in bottom navigation
    }
}