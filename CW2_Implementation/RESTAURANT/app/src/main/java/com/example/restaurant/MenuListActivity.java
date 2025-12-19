package com.example.restaurant;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView; 
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuListActivity extends AppCompatActivity {

    ImageButton btnBack;
    LinearLayout containerMenu;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        try {
            containerMenu = findViewById(R.id.containerMenu);
            if (containerMenu == null) {
                Toast.makeText(this, "ERROR: Cannot find containerMenu in XML!", Toast.LENGTH_LONG).show();
                return;
            }

            db = new DatabaseHelper(this);

            loadMenuFromDatabase();

            btnBack = findViewById(R.id.btnBack);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> finish());
            }

        } catch (Exception e) {
            Toast.makeText(this, "CRASH: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void loadMenuFromDatabase() {
        try {
            Cursor cursor = db.getAllMenuItems();

            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(this, "Menu is empty. Please uninstall & run again.", Toast.LENGTH_LONG).show();
                return;
            }

            while (cursor.moveToNext()) {

                String name = cursor.getString(1);
                String price = cursor.getString(2);
                String desc = cursor.getString(3);
                int imageResId = cursor.getInt(4);

                View cardView = LayoutInflater.from(this).inflate(R.layout.item_menu_card, containerMenu, false);

                TextView tvName = cardView.findViewById(R.id.tvFoodName);
                TextView tvPrice = cardView.findViewById(R.id.tvFoodPrice);
                TextView tvDesc = cardView.findViewById(R.id.tvFoodDesc);
                ImageView ivFood = cardView.findViewById(R.id.ivFoodImage);

                tvName.setText(name);
                tvPrice.setText("$" + price);
                tvDesc.setText(desc);

                if (imageResId != 0) {
                    ivFood.setImageResource(imageResId);
                } else {
                    ivFood.setImageResource(R.drawable.seafood_img);
                }

                containerMenu.addView(cardView);
            }

        } catch (Exception e) {
            Toast.makeText(this, "ERROR loading menu: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}