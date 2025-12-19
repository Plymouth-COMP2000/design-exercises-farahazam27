package com.example.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffLoginActivity extends AppCompatActivity {

    EditText etStaffUsername, etStaffPassword;
    TextView tvGuestLogin;
    Button btnStaffLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        db = new DatabaseHelper(this);

        etStaffUsername = findViewById(R.id.etStaffUsername);
        etStaffPassword = findViewById(R.id.etStaffPassword);
        btnStaffLogin = findViewById(R.id.btnStaffLogin);
        tvGuestLogin = findViewById(R.id.tvGuestLogin);

        tvGuestLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnStaffLogin.setOnClickListener(v -> {
            String user = etStaffUsername.getText().toString().trim();
            String pass = etStaffPassword.getText().toString().trim();

            if(user.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // SQLITE (DatabaseHelper)
                String role = db.checkLogin(user, pass);

                if (role.equals("fail")) {
                    Toast.makeText(this, "Wrong Admin Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                    prefs.edit().putString("username", user).putString("role", "staff").apply();

                    Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, StaffHomeActivity.class));
                    finish();
                }
            }
        });
    }
}