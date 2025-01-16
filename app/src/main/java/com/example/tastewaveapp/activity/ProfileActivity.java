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

public class ProfileActivity extends AppCompatActivity {

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

        profileImage = findViewById(R.id.profile_image);
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        phoneField = findViewById(R.id.phone_field);
        saveButton = findViewById(R.id.save_button);
        editPictureButton = findViewById(R.id.edit_picture_button);

        /*
        Load user profile details
        loadUserProfile();

         Save button functionality
        saveButton.setOnClickListener(v -> saveProfileChanges());

        // Change password functionality
        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Logout functionality
        logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Edit picture functionality
        editPictureButton.setOnClickListener(v -> {
            Toast.makeText(this, "Feature to edit profile picture coming soon!", Toast.LENGTH_SHORT).show();
        });


    private void loadUserProfile() {
        // Mock user details; replace with database or shared preferences retrieval
        nameField.setText("John Doe");
        emailField.setText("johndoe@example.com");
        phoneField.setText("123-456-7890");
    }

    private void saveProfileChanges() {
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save changes logic (e.g., update database)
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    */

    }
}