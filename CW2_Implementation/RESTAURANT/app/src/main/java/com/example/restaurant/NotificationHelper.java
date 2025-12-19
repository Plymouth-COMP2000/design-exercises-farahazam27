package com.example.restaurant;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "RestaurantChannel";

    public static void sendNotification(Context context, String title, String message, boolean isStaffNotification) {

        DatabaseHelper db = new DatabaseHelper(context);
        SharedPreferences prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        String receiver = isStaffNotification ? "staff" : prefs.getString("username", "Guest");
        db.addNotification(title, message, receiver);

        String settingKey;
        if (isStaffNotification) {
            settingKey = "staff_notifications_enabled"; 
        } else {
            settingKey = "notifications_enabled"; 
        }

        boolean isAllowed = prefs.getBoolean(settingKey, true);

        if (!isAllowed) {
            return;
        }

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Restaurant Updates", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        Intent intent;
        if (isStaffNotification) {
            intent = new Intent(context, StaffHomeActivity.class);
        } else {
            intent = new Intent(context, NotificationActivity.class);
        }

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi)
                .setAutoCancel(true);

        nm.notify((int) System.currentTimeMillis(), builder.build());
    }
}