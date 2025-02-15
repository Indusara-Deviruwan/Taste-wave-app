package com.example.tastewaveapp.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.UserDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.database.sqlite.SQLiteDatabase;

public class ProfileActivity extends BaseActivity {

    private ImageView profileImage;
    private EditText nameField, emailField, phoneField;
    private TextInputLayout birthdayLayout, passwordLayout, addressLayout;
    private TextInputEditText birthdayField, passwordField, addressField;
    private Button saveButton, changePasswordButton, logoutButton, editPictureButton;

    private FirebaseAuth mAuth;
    private UserDatabaseHelper userDatabaseHelper; // Your SQLite helper

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
        // Toolbar
        setupToolbar("Profile");

        // Initialize Views
        //profileImage = findViewById(R.id.profile_image);
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        phoneField = findViewById(R.id.phone_field);
        birthdayLayout = findViewById(R.id.birthday_layout);
        birthdayField = findViewById(R.id.birthday_field);
        //passwordLayout = findViewById(R.id.password_layout);
        //passwordField = findViewById(R.id.password_field);
        addressLayout = findViewById(R.id.address_layout);
        addressField = findViewById(R.id.address_field);
        saveButton = findViewById(R.id.save_button);
        editPictureButton = findViewById(R.id.edit_picture_button);

        // Initialize FirebaseAuth and SQLite helper
        mAuth = FirebaseAuth.getInstance();
        userDatabaseHelper = new UserDatabaseHelper(this);

        // Set OnClickListener for save button
        saveButton.setOnClickListener(v -> saveProfileDetails());

        // Load user data (fetch from Firebase and SQLite)
        loadUserData();
    }

    private void loadUserData() {
        // Get the current signed-in user from Firebase Authentication
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // If the user is signed in, show the Lottie animation
            LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);
            lottieAnimationView.setVisibility(View.VISIBLE); // Show the animation

            // Get the current user's email (or any unique identifier)
            String email = currentUser.getEmail();

            // Fetch user details from SQLite using the email
            Cursor cursor = userDatabaseHelper.getUser(email); // Assuming user email is stored in SQLite

            if (cursor != null && cursor.moveToFirst()) {
                // Check if the column exists and then set the text
                int nameIndex = cursor.getColumnIndex("name");
                int emailIndex = cursor.getColumnIndex("email");
                int phoneIndex = cursor.getColumnIndex("phone");
                int birthdayIndex = cursor.getColumnIndex("birthDay");
                int addressIndex = cursor.getColumnIndex("address");

                if (nameIndex != -1) {
                    nameField.setText(cursor.getString(nameIndex));
                } else {
                    Toast.makeText(this, "Name column not found", Toast.LENGTH_SHORT).show();
                }

                if (emailIndex != -1) {
                    emailField.setText(cursor.getString(emailIndex));
                } else {
                    Toast.makeText(this, "Email column not found", Toast.LENGTH_SHORT).show();
                }

                if (phoneIndex != -1) {
                    phoneField.setText(cursor.getString(phoneIndex));
                } else {
                    Toast.makeText(this, "Phone column not found", Toast.LENGTH_SHORT).show();
                }

                if (birthdayIndex != -1) {
                    birthdayField.setText(cursor.getString(birthdayIndex));
                } else {
                    Toast.makeText(this, "Birthday column not found", Toast.LENGTH_SHORT).show();
                }

                // Skip password field
                // if (passwordIndex != -1) {
                //     passwordField.setText(cursor.getString(passwordIndex)); // Do not display password
                // } else {
                //     Toast.makeText(this, "Password column not found", Toast.LENGTH_SHORT).show();
                // }

                if (addressIndex != -1) {
                    addressField.setText(cursor.getString(addressIndex));
                } else {
                    Toast.makeText(this, "Address column not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If no user is signed in, hide the Lottie animation
            LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);
            lottieAnimationView.setVisibility(View.GONE); // Hide the animation

            Toast.makeText(this, "No user is signed in", Toast.LENGTH_SHORT).show();
        }
    }



    private void saveProfileDetails() {
        // Get the input values from the fields
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();
        String birthday = birthdayField.getText().toString();
        String password = passwordField.getText().toString();
        String address = addressField.getText().toString();

        // Basic validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || birthday.isEmpty() || password.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Example: Save the profile details (you can update the SQLite database or Firebase)
        Toast.makeText(this, "Profile details saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String getToolbarTitle() {
        return "Profile";
    }

    @Override
    protected int getSelectedMenuItemId() {
        return R.id.nav_profile; // Highlight "Profile" in bottom navigation
    }
}
