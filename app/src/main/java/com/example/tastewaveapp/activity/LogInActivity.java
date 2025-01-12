package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tastewaveapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class LogInActivity extends AppCompatActivity {
    // Declare UI elements
    private LottieAnimationView lottieAnimationView;
    private CircleImageView profileImageView;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton btnSignIn, btnSignUp, btnSkip;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Make sure this matches your layout file name

        // Initialize UI components
        btnSkip = findViewById(R.id.btn_skip);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);


        // Set up button click listeners
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sign-up logic (this is just a placeholder)
                Toast.makeText(LogInActivity.this, "Redirecting to Home", Toast.LENGTH_SHORT).show();

                // Navigate to HomeActivity
                Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LogInActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle sign-in logic (this is just a placeholder)
                    Toast.makeText(LogInActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();


                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sign-up logic (this is just a placeholder)
                Toast.makeText(LogInActivity.this, "Redirecting to Sign Up", Toast.LENGTH_SHORT).show();

                // Navigate to SignUpActivity
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle forgot password logic (this is just a placeholder)
                Toast.makeText(LogInActivity.this, "Redirecting to Forgot Password", Toast.LENGTH_SHORT).show();

                // Navigate to ForgotPasswordActivity
                Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
