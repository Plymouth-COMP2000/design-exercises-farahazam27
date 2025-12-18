package com.example.restaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ManageMenuActivity extends AppCompatActivity {

    ImageButton btnBack, btnAddItem;
    LinearLayout containerMenu;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        dbHelper = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        btnAddItem = findViewById(R.id.btnAddItem);
        containerMenu = findViewById(R.id.containerMenu);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // ADD BUTTON
        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMenuActivity.this, AddMenuActivity.class);
            startActivity(intent);
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadMenu();
    }

    private void loadMenu() {
        containerMenu.removeAllViews();
        Cursor cursor = dbHelper.getAllMenuItems();

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "Menu is empty. Add item +", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int priceIndex = cursor.getColumnIndex("price");
            int descIndex = cursor.getColumnIndex("description");

            if(idIndex == -1 || nameIndex == -1) continue;

            final String itemId = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String price = cursor.getString(priceIndex);
            String desc = cursor.getString(descIndex);

            // Inflate Card
            View cardView = LayoutInflater.from(this).inflate(R.layout.item_manage_menu_card, containerMenu, false);

            TextView tvName = cardView.findViewById(R.id.tvFoodName);
            TextView tvPrice = cardView.findViewById(R.id.tvFoodPrice);
            TextView tvDesc = cardView.findViewById(R.id.tvFoodDesc);
            Button btnEdit = cardView.findViewById(R.id.btnEdit);
            Button btnDelete = cardView.findViewById(R.id.btnDelete);

            tvName.setText(name);
            tvPrice.setText("$" + price);
            tvDesc.setText(desc);

            // DELETE BUTTON
            btnDelete.setOnClickListener(v -> confirmDelete(itemId));

            // EDIT BUTTON
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(ManageMenuActivity.this, EditMenuActivity.class);
                intent.putExtra("id", itemId);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                intent.putExtra("desc", desc);
                startActivity(intent);
            });

            containerMenu.addView(cardView);
        }
        cursor.close();
    }

    private void confirmDelete(final String id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this menu item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteItem(id);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteItem(String id) {
        boolean success = dbHelper.deleteMenuItem(id);

        if (success) {
            Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
            loadMenu();
        } else {
            Toast.makeText(this, "Error deleting item", Toast.LENGTH_SHORT).show();
        }
    }
}