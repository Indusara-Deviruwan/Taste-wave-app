package com.example.tastewaveapp.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TasteWave.db";
    private static final int DATABASE_VERSION = 3;

    // User Table
    public static final String TABLE_USERS = "User";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Restaurant Table
    public static final String TABLE_RESTAURANTS = "Restaurant";
    public static final String COLUMN_RESTAURANT_ID = "restaurant_id";
    public static final String COLUMN_RESTAURANT_NAME = "restaurant_name";
    public static final String COLUMN_RESTAURANT_DESCRIPTION = "restaurant_description";
    public static final String COLUMN_RESTAURANT_IMAGE = "restaurant_image";

    // Food Table
    public static final String TABLE_FOODS = "Food";
    public static final String COLUMN_FOOD_ID = "food_id";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_FOOD_DESCRIPTION = "food_description";
    public static final String COLUMN_FOOD_IMAGE = "food_image";
    public static final String COLUMN_FOOD_PRICE = "food_price";
    public static final String COLUMN_RESTAURANT_FK = "restaurant_id_fk"; // Foreign Key

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Restaurants Table
        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + "("
                + COLUMN_RESTAURANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RESTAURANT_NAME + " TEXT,"
                + COLUMN_RESTAURANT_DESCRIPTION + " TEXT,"
                + COLUMN_RESTAURANT_IMAGE + " INTEGER" + ")";
        db.execSQL(CREATE_RESTAURANTS_TABLE);

        // Create Foods Table
        String CREATE_FOODS_TABLE = "CREATE TABLE " + TABLE_FOODS + "("
                + COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FOOD_NAME + " TEXT,"
                + COLUMN_FOOD_DESCRIPTION + " TEXT,"
                + COLUMN_FOOD_IMAGE + " INTEGER,"
                + COLUMN_FOOD_PRICE + " REAL,"
                + COLUMN_RESTAURANT_FK + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_RESTAURANT_FK + ") REFERENCES " + TABLE_RESTAURANTS + "(" + COLUMN_RESTAURANT_ID + ")" + ")";
        db.execSQL(CREATE_FOODS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        onCreate(db);
    }

    // User Operations
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Restaurant Operations
    public boolean insertRestaurant(String name, String description, int imageResId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESTAURANT_NAME, name);
        values.put(COLUMN_RESTAURANT_DESCRIPTION, description);
        values.put(COLUMN_RESTAURANT_IMAGE, imageResId);

        long result = db.insert(TABLE_RESTAURANTS, null, values);
        db.close();

        return result != -1;
    }

    public Cursor getAllRestaurants() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTS, null);
    }

    // Food Operations
    public boolean insertFood(String name, String description, int imageResId, double price, int restaurantId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_NAME, name);
        values.put(COLUMN_FOOD_DESCRIPTION, description);
        values.put(COLUMN_FOOD_IMAGE, imageResId);
        values.put(COLUMN_FOOD_PRICE, price);
        values.put(COLUMN_RESTAURANT_FK, restaurantId);

        long result = db.insert(TABLE_FOODS, null, values);
        db.close();

        return result != -1;
    }

    public Cursor getFoodsByRestaurant(int restaurantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FOODS + " WHERE " + COLUMN_RESTAURANT_FK + "=?";
        return db.rawQuery(query, new String[]{String.valueOf(restaurantId)});
    }
}
