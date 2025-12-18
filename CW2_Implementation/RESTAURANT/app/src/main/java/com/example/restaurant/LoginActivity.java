package com.example.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView btnCreateAccount, tvStaffLogin;
    ApiService apiService;

    String MY_STUDENT_ID = "BSCS2509261";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. LINK ID XML
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvStaffLogin = findViewById(R.id.tvStaffLogin); // Link tulisan kecik Staff

        // 2. SETUP API
        apiService = ApiClient.getClient().create(ApiService.class);
        setupServerDatabase();

        // 3. LOGIC LOGIN BUTTON (GUEST)
        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                performLogin(user, pass);
            }
        });

        // 4. LOGIC CREATE ACCOUNT
        btnCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // 5. LOGIC STAFF LOGIN (Pindah ke StaffLoginActivity)
        tvStaffLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, StaffLoginActivity.class));
        });
    }

    // --- SETUP DATABASE (Auto) ---
    private void setupServerDatabase() {
        apiService.createDatabase(MY_STUDENT_ID).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("API_SETUP", "Database Ready: " + response.code());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_SETUP", "Error: " + t.getMessage());
            }
        });
    }

    // --- LOGIC CHECK LOGIN GUEST (API) ---
    private void performLogin(String username, String password) {
        Toast.makeText(this, "Checking credentials...", Toast.LENGTH_SHORT).show();

        apiService.getAllUsers(MY_STUDENT_ID).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> userList = response.body().getUsers();
                    boolean found = false;
                    String role = "guest";
                    String realUsername = "";

                    for (User u : userList) {
                        if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                            found = true;
                            role = u.getUsertype();
                            realUsername = u.getUsername();
                            break;
                        }
                    }

                    if (found) {
                        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("username", realUsername);
                        editor.putString("role", role);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();

                        if (role.equalsIgnoreCase("staff") || role.equalsIgnoreCase("admin")) {
                            startActivity(new Intent(LoginActivity.this, StaffHomeActivity.class));
                        }
                        else {
                            startActivity(new Intent(LoginActivity.this, GuestHomeActivity.class));
                        }
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User Not Found / Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Connection Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}