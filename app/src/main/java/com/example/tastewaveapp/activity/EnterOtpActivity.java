package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastewaveapp.R;

public class EnterOtpActivity extends AppCompatActivity {

    private ImageButton buttonBackToForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enter_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonBackToForgotPassword = findViewById(R.id.buttonBackToForgotPassword);

        // Handle Back to Forgot Password
        buttonBackToForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(EnterOtpActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }
}