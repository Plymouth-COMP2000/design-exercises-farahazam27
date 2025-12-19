package com.example.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class StaffSettingsActivity extends AppCompatActivity {

    SwitchCompat switchNotifications;
    Button btnViewInbox, btnLogout;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_settings);

        switchNotifications = findViewById(R.id.switchNotifications);
        btnViewInbox = findViewById(R.id.btnViewInbox);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);

        boolean isEnabled = prefs.getBoolean("staff_notifications_enabled", true);

        if (switchNotifications != null) {
            switchNotifications.setChecked(isEnabled);
            switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("staff_notifications_enabled", isChecked);
                editor.apply();

                String status = isChecked ? "ON" : "OFF";
                Toast.makeText(this, "Staff Notifications: " + status, Toast.LENGTH_SHORT).show();
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                SharedPreferences.Editor editor = prefs.edit();

                editor.remove("username");
                editor.remove("role");

                editor.apply();

                Intent intent = new Intent(StaffSettingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        if (btnViewInbox != null) {
            btnViewInbox.setOnClickListener(v -> {
                Intent intent = new Intent(StaffSettingsActivity.this, NotificationActivity.class);
                startActivity(intent);
            });
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
}