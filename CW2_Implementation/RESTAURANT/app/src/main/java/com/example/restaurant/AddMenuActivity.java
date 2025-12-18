package com.example.restaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddMenuActivity extends AppCompatActivity {

    EditText etName, etPrice, etDesc;
    Button btnUpload, btnAdd;
    ImageButton btnBack;
    ImageView ivPreview;
    TextView tvUploadPlaceholder;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etItemName);
        etPrice = findViewById(R.id.etItemPrice);
        etDesc = findViewById(R.id.etItemDesc);
        btnUpload = findViewById(R.id.btnUpload);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        ivPreview = findViewById(R.id.ivPreview);
        tvUploadPlaceholder = findViewById(R.id.tvUploadPlaceholder);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        btnUpload.setOnClickListener(v -> {
            ivPreview.setVisibility(View.VISIBLE);
            ivPreview.setImageResource(android.R.drawable.ic_menu_gallery);
            tvUploadPlaceholder.setVisibility(View.GONE);

            Toast.makeText(this, "Picture Uploaded!", Toast.LENGTH_SHORT).show();
        });

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String price = etPrice.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (name.isEmpty() || price.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to DB
            boolean success = db.addMenuItem(name, price, desc);

            if (success) {
                Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }
}