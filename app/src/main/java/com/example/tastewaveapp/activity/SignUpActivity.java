package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.editTextSignUpEmail);
        passwordEditText = findViewById(R.id.editTextSignUpPassword);
        confirmPasswordEditText = findViewById(R.id.editTextSignUpConfirmPassword);
        signUpButton = findViewById(R.id.buttonSignUp);
        databaseHelper = new DatabaseHelper(this);

        signUpButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (databaseHelper.checkUser(email, password)) {
                Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = databaseHelper.insertUser(email, password);
                if (isInserted) {
                    Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Error occurred during sign-up", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back button to navigate to Sign In
        ImageButton backToLogInButton = findViewById(R.id.buttonBackToLogIn);
        backToLogInButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
            finish(); // Optional: Closes this activity
        });
    }
}
