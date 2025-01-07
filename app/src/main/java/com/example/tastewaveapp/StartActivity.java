package com.example.tastewaveapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@SuppressLint("CustomSplashScreen")
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        //attach button to a button object
        Button btnSkip = findViewById(R.id.btn_skip);
        Button btnContinue = findViewById(R.id.btn_continue);

        btnSkip.setOnClickListener(view -> {
            //when click on skip button go to the home screen
            Intent intent = new Intent(StartActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();//prevent going back to the login screen
        });

        btnContinue.setOnClickListener(view -> {
            //when click on continue button go to the login screen
            Intent intent = new Intent(StartActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();//prevent going back to the login screen

        });
    }
}