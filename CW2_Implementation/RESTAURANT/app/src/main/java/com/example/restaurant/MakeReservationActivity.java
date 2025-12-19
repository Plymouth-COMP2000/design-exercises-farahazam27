package com.example.restaurant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Locale;

public class MakeReservationActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText etName;
    TextView tvErrorName;
    LinearLayout layoutDate, layoutTime; 
    TextView tvDate, tvTime;

    Button btnMinus, btnPlus;
    TextView tvGuestCount;
    int guestCount = 1; 

    Button btnReserveConfirm;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);

        db = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        etName = findViewById(R.id.etName);
        tvErrorName = findViewById(R.id.tvErrorName); 
        layoutDate = findViewById(R.id.layoutDate);
        tvDate = findViewById(R.id.tvDate);

        layoutTime = findViewById(R.id.layoutTime);
        tvTime = findViewById(R.id.tvTime);

        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        tvGuestCount = findViewById(R.id.tvGuestCount);

        btnReserveConfirm = findViewById(R.id.btnReserveConfirm);

        tvGuestCount.setText(String.valueOf(guestCount));
    
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String savedName = prefs.getString("username", "");
        if (!savedName.isEmpty()) {
            etName.setText(savedName);
        }

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        layoutDate.setOnClickListener(v -> showDatePicker());

        layoutTime.setOnClickListener(v -> showTimePicker());

        btnMinus.setOnClickListener(v -> {
            if (guestCount > 1) {
                guestCount--;
                tvGuestCount.setText(String.valueOf(guestCount));
            }
        });

        btnPlus.setOnClickListener(v -> {
            if (guestCount < 20) { // Limit max 20 persons
                guestCount++;
                tvGuestCount.setText(String.valueOf(guestCount));
            }
        });

        // Reserve Button (Trigger Popup)
        btnReserveConfirm.setOnClickListener(v -> {
            if (isValidInput()) {
                showConfirmDialog();
            }
        });
    }

    // HELPER: PICKERS
    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            tvDate.setText(date);
            tvDate.setTextColor(Color.BLACK); 
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String amPm = hourOfDay >= 12 ? "PM" : "AM";
            int hour = hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;
            if (hour == 0) hour = 12;
            String time = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm);
            tvTime.setText(time);
            tvTime.setTextColor(Color.BLACK);
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
    }

    // Validation
    private boolean isValidInput() {
        boolean valid = true;

        // Check Name
        if (etName.getText().toString().trim().isEmpty()) {
            tvErrorName.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            tvErrorName.setVisibility(View.GONE);
        }

        // Check Date
        if (tvDate.getText().toString().equals("Select Date")) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        // Check Time
        if (tvTime.getText().toString().equals("Select Time")) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void showConfirmDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm_booking);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnEdit = dialog.findViewById(R.id.btnEdit);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnEdit.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            processBooking();
            dialog.dismiss();
            showSuccessDialog();
        });

        dialog.show();
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false); 

        Button btnOk = dialog.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(MakeReservationActivity.this, MyReservationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }

    private void processBooking() {
        String name = etName.getText().toString().trim();
        String date = tvDate.getText().toString();
        String time = tvTime.getText().toString();
        String guests = String.valueOf(guestCount); 

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = prefs.getString("username", "Guest");

        boolean success = db.insertReservation(name, date, time, guests, username);

        if (success) {
            NotificationHelper.sendNotification(this, "New Reservation", "New booking from " + name, true);
        } else {
            Toast.makeText(this, "Booking failed (Database Error)", Toast.LENGTH_SHORT).show();
        }
    }
}