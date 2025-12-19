package com.example.restaurant;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences; 
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.NotificationChannel; 
import android.app.NotificationManager; 
import android.content.Context; 
import androidx.core.app.NotificationCompat; 

public class GuestHomeActivity extends AppCompatActivity {

    Button btnMenu, btnReservation, btnSettings;
    TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        // Permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        btnMenu = findViewById(R.id.btnMenu);
        btnReservation = findViewById(R.id.btnReservation);
        btnSettings = findViewById(R.id.btnSettings);
        tvWelcome = findViewById(R.id.tvWelcome);

        btnMenu.setOnClickListener(v -> startActivity(new Intent(GuestHomeActivity.this, MenuListActivity.class)));
        btnReservation.setOnClickListener(v -> startActivity(new Intent(GuestHomeActivity.this, MyReservationActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(GuestHomeActivity.this, SettingsActivity.class)));

    }

    private void sendNotification(String title, String message) {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isAllowed = prefs.getBoolean("notifications_enabled", true);

        if (!isAllowed) {
            return;
        }

        String channelId = "HospitalityChannel";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Guest Updates", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground) 
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify(1, builder.build());
    }
}