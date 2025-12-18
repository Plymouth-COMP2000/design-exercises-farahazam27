package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffHomeActivity extends AppCompatActivity {

    Button btnManageMenu, btnViewBookings, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        // 1. Link Buttons
        btnManageMenu = findViewById(R.id.btnManageMenu);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnSettings = findViewById(R.id.btnSettings);

        // 2. Manage Menu Logic
        btnManageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffHomeActivity.this, ManageMenuActivity.class);
                startActivity(intent);
            }
        });

        // 3. View Reservations Logic
        btnViewBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffHomeActivity.this, ViewReservationsActivity.class);
                startActivity(intent);
            }
        });

        // 4. Settings Logic
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffHomeActivity.this, StaffSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}