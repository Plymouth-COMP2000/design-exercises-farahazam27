package com.example.restaurant;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etPhone, etUsername, etPassword;
    Button btnSignup;
    TextView tvLoginLink;
    ApiService apiService;

    // MY STUDENT ID
    String MY_STUDENT_ID = "BSCS2509261";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. LINK ID
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        // 2. API SETUP
        apiService = ApiClient.getClient().create(ApiService.class);

        // 3. LOGIC LOGIN LINK (Back Button)
        tvLoginLink.setOnClickListener(v -> {
            finish();
        });

        // 4. LOGIC SIGNUP BUTTON
        btnSignup.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (fullName.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                performRegister(fullName, phone, username, password);
            }
        });
    }

    private void performRegister(String fullName, String phone, String username, String password) {
        Toast.makeText(this, "Creating Account...", Toast.LENGTH_SHORT).show();

        String firstName = fullName;
        String lastName = "User"; // Default Last Name

        if (fullName.contains(" ")) {
            int splitIndex = fullName.lastIndexOf(" ");
            firstName = fullName.substring(0, splitIndex);
            lastName = fullName.substring(splitIndex + 1);
        }

        // --- AUTO-GENERATE EMAIL ---
        String email = username + "@student.plymouth.ac.uk";
        String userType = "student"; // Default role Guest

        // Insert Data into Model User
        User newUser = new User(
                username,
                password,
                firstName,
                lastName,
                email,
                phone,
                userType
        );

        // Send Data to API Server
        apiService.registerUser(MY_STUDENT_ID, newUser).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Signup Success! Please Login.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}