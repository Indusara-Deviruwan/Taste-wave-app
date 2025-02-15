package com.example.tastewaveapp.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "restaurants.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESTAURANTS = "restaurants";
    public static final String COLUMN_ID = "restaurant_id";
    public static final String COLUMN_NAME = "restaurant_name";
    public static final String COLUMN_DESCRIPTION = "restaurant_description";
    public static final String COLUMN_IMAGE_URL = "restaurant_image";

    public RestaurantDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT)";
        db.execSQL(CREATE_RESTAURANTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        onCreate(db);
    }

    public boolean insertRestaurant(String name, String description, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_RESTAURANTS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllRestaurants() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTS, null);
    }

    // Method to get restaurants by name (case-insensitive)
    public Cursor getRestaurantsByName(String restaurantName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RESTAURANTS + " WHERE " + COLUMN_NAME + " LIKE ?";
        return db.rawQuery(query, new String[]{"%" + restaurantName + "%"});
    }
}
