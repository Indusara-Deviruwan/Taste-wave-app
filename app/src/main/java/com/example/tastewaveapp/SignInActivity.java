package com.example.tastewaveapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signInButton, signUpButton; // Added signUpButton
    private TextView forgotPasswordTextView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        signInButton = findViewById(R.id.btn_SignIn);
        signUpButton = findViewById(R.id.btn_SignUp);// Reference the Sign Up button
        forgotPasswordTextView = findViewById(R.id.textView11);
        databaseHelper = new DatabaseHelper(this);


        // Handle Sign In
        signInButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isValid = databaseHelper.checkUser(email, password);
                if (isValid) {
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.putExtra("email", email); // Pass user email to HomeActivity
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle Sign Up
        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent); // Navigate to the Sign Up Activity
            finish();
        });

        forgotPasswordTextView.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });

        Button skipButton = findViewById(R.id.btn_Skip);
        skipButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
