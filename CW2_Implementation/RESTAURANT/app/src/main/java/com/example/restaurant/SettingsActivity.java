package com.example.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat switchNotifications;
    Button btnViewInbox, btnLogout;
    View btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switchNotifications);
        btnViewInbox = findViewById(R.id.btnViewInbox);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);

        if (switchNotifications != null) {
            // Load status lama. Default = TRUE (ON)
            boolean isEnabled = prefs.getBoolean("notifications_enabled", true);
            switchNotifications.setChecked(isEnabled);

            switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("notifications_enabled", isChecked);
                editor.apply();

                if (isChecked) {
                    Toast.makeText(this, "Notifications ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notifications OFF", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // =========================================================
        // LOGIC BUTTON LAIN
        // =========================================================

        // Button Inbox
        if (btnViewInbox != null) {
            btnViewInbox.setOnClickListener(v -> {
                // Pastikan nama class ni betul (NotificationActivity atau NotificationInboxActivity)
                startActivity(new Intent(this, NotificationActivity.class));
            });
        }

        // Button Logout (Tambah Confirmation Popup)
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Clear session & balik Login
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }

        // Button Back
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
}