package com.example.restaurant; // CHANGE THIS to your actual package name (e.g., com.example.flora)

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MakeReservationActivity extends AppCompatActivity {

    // 1. Declare Variables
    LinearLayout layoutDate, layoutTime;
    TextView tvDate, tvTime, tvErrorName;
    EditText etName;
    Button btnReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);

        // 2. Link Variables to XML IDs
        layoutDate = findViewById(R.id.layoutDate);
        layoutTime = findViewById(R.id.layoutTime);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        etName = findViewById(R.id.etName);
        tvErrorName = findViewById(R.id.tvErrorName); // The Red Error Message
        btnReserve = findViewById(R.id.btnReserveConfirm);

        // Initially hide the error message
        tvErrorName.setVisibility(View.GONE);

        // 3. Date Picker Listener (The Auto Calendar)
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // 4. Time Picker Listener (The Auto Clock)
        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // 5. Reserve Button Listener (Validation & Success)
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptReservation();
            }
        });
    }

    // --- HELPER 1: SHOW DATE PICKER ---
    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        tvDate.setTextColor(Color.BLACK); // Make text black to show it is selected
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    // --- HELPER 2: SHOW TIME PICKER ---
    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                        tvTime.setText(formattedTime);
                        tvTime.setTextColor(Color.BLACK);
                    }
                },
                hour, minute, false);
        timePickerDialog.show();
    }

    // --- HELPER 3: VALIDATE & SHOW SUCCESS ---
    private void attemptReservation() {
        String nameInput = etName.getText().toString();

        // CHECK: Did the user leave the name empty?
        if (nameInput.isEmpty()) {
            // YES: Show the Error Message (Exercise 4 Requirement)
            tvErrorName.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else {
            // NO: Hide error and Show Success Popup
            tvErrorName.setVisibility(View.GONE);
            showSuccessDialog();
        }
    }

    // --- HELPER 4: SHOW CUSTOM SUCCESS DIALOG ---
    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success); // This connects to your success popup XML
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish(); // Closes the screen and goes back to menu
            }
        });

        dialog.show();
    }
}