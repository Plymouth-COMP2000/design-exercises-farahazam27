package com.example.restaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RestaurantFinalV15.db";
    public static final int DATABASE_VERSION = 15;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_MENU = "menu_items";
    public static final String TABLE_RESERVATIONS = "reservations";
    public static final String TABLE_NOTIFICATIONS = "notifications";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table Users
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, phone TEXT, role TEXT)");

        // Table Menu
        db.execSQL("CREATE TABLE " + TABLE_MENU + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price TEXT, description TEXT, image_resource INTEGER)");

        // Table Reservations
        db.execSQL("CREATE TABLE " + TABLE_RESERVATIONS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date TEXT, time TEXT, guests TEXT, status TEXT, userid TEXT)");

        // Table Notifications
        db.execSQL("CREATE TABLE " + TABLE_NOTIFICATIONS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, message TEXT, receiver TEXT, date TEXT, is_read INTEGER DEFAULT 0)");

        // --- INSERT DEFAULT DATA ---
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password, role) VALUES ('admin', '1234', 'staff')");

        // Menu Items Default
        addDefaultMenu(db, "Seafood Noodles", "15.00", "Spicy broth with fresh prawns.", R.drawable.seafood_img);
        addDefaultMenu(db, "Mee Curry", "12.00", "Vegetable curry with noodles.", R.drawable.curry_img);
        addDefaultMenu(db, "Biryani Rice", "18.00", "Aromatic South Asian layered rice dish.", R.drawable.biryani_img);
    }

    private void addDefaultMenu(SQLiteDatabase db, String name, String price, String desc, int img) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("description", desc);
        cv.put("image_resource", img);
        db.insert(TABLE_MENU, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(db);
    }

    // CRUD METHODS FOR MENU 
    public boolean addMenuItem(String name, String price, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", desc);
        values.put("image_resource", R.drawable.seafood_img);
        long result = db.insert(TABLE_MENU, null, values);
        return result != -1;
    }

    public boolean updateMenuItem(String id, String name, String price, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", desc);
        long result = db.update(TABLE_MENU, values, "id=?", new String[]{id});
        return result > 0;
    }

    public boolean deleteMenuItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, "id=?", new String[]{id}) > 0;
    }

    public Cursor getAllMenuItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MENU, null);
    }

    // CRUD METHODS FOR RESERVATIONS 
    public boolean insertReservation(String name, String date, String time, String guests, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("date", date);
        values.put("time", time);
        values.put("guests", guests);
        values.put("status", "Pending");
        values.put("userid", userid);
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        return result != -1;
    }

    public Cursor getAllReservations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, * FROM " + TABLE_RESERVATIONS + " ORDER BY id DESC", null);
    }

    public boolean deleteReservation(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_RESERVATIONS, "id=?", new String[]{id}) > 0;
    }

    public boolean updateReservationStatus(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        return db.update(TABLE_RESERVATIONS, values, "id=?", new String[]{id}) > 0;
    }

    // CRUD METHODS FOR NOTIFICATIONS
    public boolean addNotification(String title, String message, String receiver) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("message", message);
        values.put("receiver", receiver);
        values.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        values.put("is_read", 0);
        long result = db.insert(TABLE_NOTIFICATIONS, null, values);
        return result != -1;
    }

    public Cursor getNotifications(String receiverName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS + " WHERE receiver=? ORDER BY id DESC", new String[]{receiverName});
    }

    // AUTHENTICATION
    public String checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM " + TABLE_USERS + " WHERE username=? AND password=?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return "fail";
    }

    public boolean registerGuest(String username, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        contentValues.put("role", "guest");
        return db.insert(TABLE_USERS, null, contentValues) != -1;
    }
}