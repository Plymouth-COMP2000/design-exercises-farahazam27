package com.example.restaurant;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MakeReservationActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText etName;
    TextView tvDate, tvTime, tvGuestCount;
    LinearLayout layoutDate, layoutTime;
    Button btnMinus, btnPlus, btnReserveConfirm;
    int guestCount = 1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);

        db = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        etName = findViewById(R.id.etName);
        layoutDate = findViewById(R.id.layoutDate);
        tvDate = findViewById(R.id.tvDate);
        layoutTime = findViewById(R.id.layoutTime);
        tvTime = findViewById(R.id.tvTime);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        tvGuestCount = findViewById(R.id.tvGuestCount);
        btnReserveConfirm = findViewById(R.id.btnReserveConfirm);

        btnBack.setOnClickListener(v -> finish());

        tvGuestCount.setText("1");
        btnPlus.setOnClickListener(v -> { guestCount++; tvGuestCount.setText(String.valueOf(guestCount)); });
        btnMinus.setOnClickListener(v -> { if(guestCount > 1) { guestCount--; tvGuestCount.setText(String.valueOf(guestCount)); }});

        layoutDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> tvDate.setText(d + "/" + (m+1) + "/" + y), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        layoutTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, h, m) -> tvTime.setText(String.format("%02d:%02d", h, m)), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        btnReserveConfirm.setOnClickListener(v -> {
            String nameInForm = etName.getText().toString();
            String date = tvDate.getText().toString();
            String time = tvTime.getText().toString();

            if (nameInForm.isEmpty() || date.contains("Select") || time.contains("Select")) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            String loggedInUsername = prefs.getString("username", "");

            if (db.insertReservation(nameInForm, date, time, String.valueOf(guestCount), loggedInUsername)) {

                db.addNotification("New Reservation", "New booking from " + nameInForm, "staff");
                NotificationHelper.sendNotification(this, "New Reservation", "New booking received.");

                Toast.makeText(this, "Sent to Staff!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}