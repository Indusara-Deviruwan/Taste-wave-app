package com.example.tastewaveapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastewaveapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(StartActivity.this, HomeActivity.class);
            startActivity(intent);


        }
    }

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
            Intent intent = new Intent(StartActivity.this, HomeActivity.class);
            startActivity(intent);
            //prevent going back to the login screen
        });

        btnContinue.setOnClickListener(view -> {
            //when click on continue button go to the login screen
            Intent intent = new Intent(StartActivity.this, LogInActivity.class);
            startActivity(intent);
            //prevent going back to the login screen

        });
    }
}