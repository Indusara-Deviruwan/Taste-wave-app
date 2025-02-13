package com.example.tastewaveapp.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tastewaveapp.model.FoodCart;

import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CART = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_IMAGE_URL = "imageUrl";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_IMAGE_URL + " TEXT)";
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public void addToCart(FoodCart food) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if item already exists in the cart
        Cursor cursor = db.query(TABLE_CART, new String[]{COLUMN_ID, COLUMN_QUANTITY},
                COLUMN_NAME + "=?", new String[]{food.getName()}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Item already exists, update quantity
            int existingId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            int existingQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
            cursor.close();

            updateCartItemQuantity(existingId, existingQuantity + food.getQuantity());
        } else {
            // Insert new item
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, food.getName());
            values.put(COLUMN_DESCRIPTION, food.getDescription());
            values.put(COLUMN_PRICE, food.getPrice());
            values.put(COLUMN_QUANTITY, food.getQuantity());
            values.put(COLUMN_IMAGE_URL, food.getImageUrl());

            db.insert(TABLE_CART, null, values);
        }

        db.close();
    }

    public List<FoodCart> getAllCartItems() {
        List<FoodCart> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CART,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_PRICE, COLUMN_QUANTITY, COLUMN_IMAGE_URL},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));

                cartItems.add(new FoodCart(id, name, description, price, quantity, imageUrl));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return cartItems;
    }

    public void removeItemFromCart(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateCartItemQuantity(int id, int newQuantity) {
        if (newQuantity <= 0) {
            removeItemFromCart(id);
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);

        db.update(TABLE_CART, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public double getCartTotalPrice() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalPrice = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_PRICE + " * " + COLUMN_QUANTITY + ") FROM " + TABLE_CART, null);
        if (cursor != null && cursor.moveToFirst()) {
            totalPrice = cursor.getDouble(0);
            cursor.close();
        }
        db.close();
        return totalPrice;
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
        db.execSQL("VACUUM");  // Resets the database size and reclaims unused space
        db.close();
    }
}
