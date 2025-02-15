// Updated SignUpActivity to use the new User model and UserDatabaseHelper
package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tastewaveapp.R;
import com.example.tastewaveapp.databasehelper.UserDatabaseHelper;
import com.example.tastewaveapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, birthdayEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox agreeCheckBox;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        dbHelper = new UserDatabaseHelper(this);

        nameEditText = findViewById(R.id.editTextSignUpName);
        phoneEditText = findViewById(R.id.editTextSignUpPhone);
        addressEditText = findViewById(R.id.editTextSignUpAddress);
        birthdayEditText = findViewById(R.id.editTextSignUpBirthday);
        emailEditText = findViewById(R.id.editTextSignUpEmail);
        passwordEditText = findViewById(R.id.editTextSignUpPassword);
        confirmPasswordEditText = findViewById(R.id.editTextSignUpConfirmPassword);
        agreeCheckBox = findViewById(R.id.checkBoxAgreeTerms);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.buttonSignUp).setOnClickListener(view -> registerUser());
        ImageButton backToLogInButton = findViewById(R.id.buttonBackToLogIn);
        backToLogInButton.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String birthday = birthdayEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || birthday.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!agreeCheckBox.isChecked()) {
            Toast.makeText(this, "You must agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                User user = new User(0, name, email, password, phone, address, birthday);
                dbHelper.insertUser(user);
                Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
