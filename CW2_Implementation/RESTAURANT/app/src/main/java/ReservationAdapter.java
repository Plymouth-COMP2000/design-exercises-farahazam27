package com.example.restaurant;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

public class ReservationAdapter extends CursorAdapter {

    public ReservationAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_reservation_card, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    
        TextView tvStatus = view.findViewById(R.id.tvResStatus);
        TextView tvDate = view.findViewById(R.id.tvResDate);
        TextView tvTime = view.findViewById(R.id.tvResTime);
        TextView tvPax = view.findViewById(R.id.tvResPax);
        TextView tvName = view.findViewById(R.id.tvResName);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        String guests = cursor.getString(cursor.getColumnIndexOrThrow("guests"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

        tvStatus.setText(status);
        tvDate.setText(date);
        tvTime.setText(time);
        tvPax.setText(guests + " Guests");
        tvName.setText("Booked by: " + name);

        if ("Approved".equalsIgnoreCase(status)) {
            tvStatus.setBackgroundColor(0xFF4CAF50); // Green
        } else if ("Rejected".equalsIgnoreCase(status)) {
            tvStatus.setBackgroundColor(0xFFF44336); // Red
        } else {
            tvStatus.setBackgroundColor(0xFFFF6F3C); // Orange
        }

        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Reservation")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DatabaseHelper db = new DatabaseHelper(context);
                        db.deleteReservation(id);
                        Toast.makeText(context, "Reservation Cancelled", Toast.LENGTH_SHORT).show();

                        if (context instanceof MyReservationActivity) {
                            ((MyReservationActivity) context).loadReservations();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}