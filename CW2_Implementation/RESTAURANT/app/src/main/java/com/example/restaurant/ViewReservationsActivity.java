package com.example.restaurant;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ViewReservationsActivity extends AppCompatActivity {
    LinearLayout containerRes;
    DatabaseHelper db;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations);
        db = new DatabaseHelper(this);
        containerRes = findViewById(R.id.containerRes);
        btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        loadAllReservations();
    }

    private void loadAllReservations() {
        containerRes.removeAllViews();
        Cursor cursor = db.getAllReservations();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String guests = cursor.getString(cursor.getColumnIndexOrThrow("guests"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String realUserid = cursor.getString(cursor.getColumnIndexOrThrow("userid"));

                View card = LayoutInflater.from(this).inflate(R.layout.item_reservation, containerRes, false);
                TextView tvName = card.findViewById(R.id.tvResName);
                TextView tvStatus = card.findViewById(R.id.tvResStatus);
                Button btnApprove = card.findViewById(R.id.btnApproveRes);
                Button btnCancel = card.findViewById(R.id.btnCancelRes);

                tvName.setText(name + " (" + guests + " Pax)\n" + date + " " + time);

                if (status.equalsIgnoreCase("Confirmed")) {
                    tvStatus.setText("Confirmed");
                    tvStatus.setTextColor(Color.GREEN);
                    btnApprove.setVisibility(View.GONE);
                } else {
                    tvStatus.setText("Pending");
                    tvStatus.setTextColor(Color.parseColor("#FFA500"));
                    btnApprove.setVisibility(View.VISIBLE);
                }

                btnApprove.setOnClickListener(v -> {
                    if (db.updateReservationStatus(id, "Confirmed")) {
                        db.addNotification("Approved ✅", "Your booking on " + date + " is confirmed.", realUserid);
                        NotificationHelper.sendNotification(this, "Approved", "Booking confirmed for " + name);
                        Toast.makeText(this, "Approved!", Toast.LENGTH_SHORT).show();
                        loadAllReservations();
                    }
                });

                btnCancel.setOnClickListener(v -> {
                    new AlertDialog.Builder(this).setTitle("Cancel").setMessage("Reject this booking?")
                            .setPositiveButton("Yes", (d, w) -> {
                                if (db.deleteReservation(id)) {
                                    loadAllReservations();
                                }
                            }).setNegativeButton("No", null).show();
                });
                containerRes.addView(card);
            }
            cursor.close();
        }
    }
}