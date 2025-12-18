package com.example.restaurant;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    ListView lvNotifications;
    DatabaseHelper db;
    View btnBack;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        db = new DatabaseHelper(this);
        lvNotifications = findViewById(R.id.lvNotifications);
        tvTitle = findViewById(R.id.tvPageTitle);
        btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        loadInbox();
    }

    private void loadInbox() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String role = prefs.getString("role", "guest");
        String username = prefs.getString("username", "");

        String key = role.equals("staff") ? "staff" : username;

        if(tvTitle != null) {
            tvTitle.setText(role.equals("staff") ? "STAFF INBOX" : "MY INBOX");
        }

        Cursor c = db.getNotifications(key);
        ArrayList<String> list = new ArrayList<>();

        if (c != null) {
            while (c.moveToNext()) {
                String title = c.getString(c.getColumnIndexOrThrow("title"));
                String msg = c.getString(c.getColumnIndexOrThrow("message"));
                list.add("📌 " + title + "\n" + msg);
            }
            c.close();
        }

        if (list.isEmpty()) {
            list.add("No notifications found.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lvNotifications.setAdapter(adapter);
    }
}