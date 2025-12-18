package com.example.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MyReservationActivity extends AppCompatActivity {
    LinearLayout containerReservations, layoutEmptyState;
    ScrollView layoutListState;
    DatabaseHelper db;
    ImageButton btnBack;
    Button btnMakeReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);

        containerReservations = findViewById(R.id.containerReservations);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        layoutListState = findViewById(R.id.layoutListState);
        btnBack = findViewById(R.id.btnBack);
        btnMakeReservation = findViewById(R.id.btnMakeReservation);
        db = new DatabaseHelper(this);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        if (btnMakeReservation != null) btnMakeReservation.setOnClickListener(v -> {
            startActivity(new Intent(this, MakeReservationActivity.class));
            finish();
        });

        loadMyReservations();
    }

    private void loadMyReservations() {
        containerReservations.removeAllViews();
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String myLoginUsername = prefs.getString("username", "");

        Cursor cursor = db.getAllReservations();
        boolean hasBooking = false;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String useridInDb = cursor.getString(cursor.getColumnIndexOrThrow("userid"));

                if (!useridInDb.equals(myLoginUsername)) continue;

                hasBooking = true;

                View card = LayoutInflater.from(this).inflate(R.layout.item_reservation, containerReservations, false);
                TextView tvName = card.findViewById(R.id.tvResName);
                TextView tvStatus = card.findViewById(R.id.tvResStatus);
                Button btnCancel = card.findViewById(R.id.btnCancelRes);
                Button btnApprove = card.findViewById(R.id.btnApproveRes);

                tvName.setText(name + "\n" + date + " " + time);
                if (btnApprove != null) btnApprove.setVisibility(View.GONE);

                if (status.equalsIgnoreCase("Confirmed")) {
                    tvStatus.setText("Confirmed");
                    tvStatus.setTextColor(Color.GREEN);
                } else {
                    tvStatus.setText("Pending");
                    tvStatus.setTextColor(Color.parseColor("#FFA500"));
                }

                btnCancel.setOnClickListener(v -> {
                    new AlertDialog.Builder(this).setTitle("Cancel").setMessage("Delete this booking?")
                            .setPositiveButton("Yes", (d, w) -> {
                                if (db.deleteReservation(id)) {
                                    db.addNotification("Cancelled", name + " cancelled booking.", "staff");
                                    NotificationHelper.sendNotification(this, "Cancelled", "Booking cancelled.");
                                    loadMyReservations();
                                }
                            }).setNegativeButton("No", null).show();
                });
                containerReservations.addView(card);
            }
            cursor.close();
        }

        if (hasBooking) {
            layoutListState.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
        } else {
            layoutListState.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        }
    }
}