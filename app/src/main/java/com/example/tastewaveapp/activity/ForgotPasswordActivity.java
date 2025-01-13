package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.DatabaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText, editTextForgotPasswordPhone;
    private ImageButton buttonBackToSignIn;
    private Button buttonSendDetails;
    private DatabaseHelper databaseHelper; // Assuming you have a DatabaseHelper for user management.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.editTextForgotPasswordEmail);
        editTextForgotPasswordPhone = findViewById(R.id.editTextForgotPasswordPhone);
        buttonBackToSignIn = findViewById(R.id.buttonBackToSignIn);
        buttonSendDetails = findViewById(R.id.buttonSendDetails);


        databaseHelper = new DatabaseHelper(this);

        // Handle Reset Password
    /*    resetPasswordButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else if (databaseHelper.isEmailRegistered(email)) { // Check if email exists in DB
                // For simplicity, just show a message
                Toast.makeText(ForgotPasswordActivity.this,
                        "Password reset instructions have been sent to your email.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ForgotPasswordActivity.this,
                        "Email is not registered",
                        Toast.LENGTH_SHORT).show();
            }
        }); */

        // Handle Back to Sign In
        buttonBackToSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        });

        buttonSendDetails.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, EnterOtpActivity.class);
            startActivity(intent);
            finish();
        });

    }

}