package com.example.restaurant;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditMenuActivity extends AppCompatActivity {

    EditText etName, etPrice, etDesc;
    Button btnSaveChanges;
    ImageButton btnBack;
    DatabaseHelper db;
    String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etEditName);
        etPrice = findViewById(R.id.etEditPrice);
        etDesc = findViewById(R.id.etEditDesc);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("id")) {
            itemId = getIntent().getStringExtra("id");
            etName.setText(getIntent().getStringExtra("name"));
            etPrice.setText(getIntent().getStringExtra("price"));
            etDesc.setText(getIntent().getStringExtra("desc"));
        }

        btnSaveChanges.setOnClickListener(v -> {
            if(isValid()) {
                showConfirmPopup();
            }
        });
    }

    private void showConfirmPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm_update); // Reusing your Gold Popup
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            updateData();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateData() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();

        boolean success = db.updateMenuItem(itemId, name, price, desc);

        if (success) {
            Toast.makeText(this, "Menu Item Successfully Updated!", Toast.LENGTH_LONG).show();
            finish(); // Go back to Manage Menu
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {
        if (etName.getText().toString().isEmpty() || etPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Name and Price are required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}