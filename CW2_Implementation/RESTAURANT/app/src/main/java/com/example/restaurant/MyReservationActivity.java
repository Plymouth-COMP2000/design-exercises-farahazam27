package com.example.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyReservationActivity extends AppCompatActivity {

    ListView lvReservations;
    LinearLayout layoutEmpty;
    Button btnMakeFirstReservation;
    FloatingActionButton fabAdd;
    ImageButton btnBack;
    DatabaseHelper db;
    ReservationAdapter adapter; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);

        db = new DatabaseHelper(this);

        lvReservations = findViewById(R.id.lvReservations);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        btnMakeFirstReservation = findViewById(R.id.btnMakeFirstReservation);
        fabAdd = findViewById(R.id.fabAdd);
        btnBack = findViewById(R.id.btnBack);

        loadReservations(); 

        btnMakeFirstReservation.setOnClickListener(v -> goToBooking());
        fabAdd.setOnClickListener(v -> goToBooking());
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    private void goToBooking() {
        startActivity(new Intent(MyReservationActivity.this, MakeReservationActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservations(); 
    }

    public void loadReservations() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = prefs.getString("username", "");

        Cursor cursor = db.getAllReservations(); 

        if (cursor == null || cursor.getCount() == 0) {
            layoutEmpty.setVisibility(View.VISIBLE);
            lvReservations.setVisibility(View.GONE);
            fabAdd.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            lvReservations.setVisibility(View.VISIBLE);
            fabAdd.setVisibility(View.VISIBLE);

            adapter = new ReservationAdapter(this, cursor);
            lvReservations.setAdapter(adapter);
        }
    }
}