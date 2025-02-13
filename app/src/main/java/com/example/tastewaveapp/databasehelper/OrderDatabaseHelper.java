package com.example.tastewaveapp.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tastewaveapp.model.Order;
import com.example.tastewaveapp.model.FoodCart;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "orders.db";
    private static final int DATABASE_VERSION = 2;  // Increment version number for the new schema

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ID = "order_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TOTAL_PRICE = "total_price";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_DELIVERY_ADDRESS = "delivery_address";

    // Additional table for food items
    public static final String TABLE_FOOD_ITEMS = "food_items";
    public static final String COLUMN_FOOD_ITEM_ID = "food_item_id";
    public static final String COLUMN_ORDER_ID_FOOD = "order_id";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_FOOD_QUANTITY = "food_quantity";

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_TOTAL_PRICE + " REAL, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_DELIVERY_ADDRESS + " TEXT)";

        String CREATE_FOOD_ITEMS_TABLE = "CREATE TABLE " + TABLE_FOOD_ITEMS + " (" +
                COLUMN_FOOD_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_ID_FOOD + " INTEGER, " +
                COLUMN_FOOD_NAME + " TEXT, " +
                COLUMN_FOOD_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ORDER_ID_FOOD + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ID + "))";

        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_FOOD_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_ITEMS);
            onCreate(db);
        }
    }

    public boolean insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, order.getUserId());
        values.put(COLUMN_TOTAL_PRICE, order.getTotalPrice());
        values.put(COLUMN_STATUS, order.getOrderStatus());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_DELIVERY_ADDRESS, order.getDeliveryAddress());

        long result = db.insert(TABLE_ORDERS, null, values);

        if (result == -1) {
            db.close();
            return false; // Order insertion failed
        }

        // Insert each food item associated with the order
        long orderId = result; // The generated orderId from the insert
        for (FoodCart foodItem : order.getFoodItems()) {
            ContentValues foodItemValues = new ContentValues();
            foodItemValues.put(COLUMN_ORDER_ID_FOOD, orderId);
            foodItemValues.put(COLUMN_FOOD_NAME, foodItem.getName());
            foodItemValues.put(COLUMN_FOOD_QUANTITY, foodItem.getQuantity());

            db.insert(TABLE_FOOD_ITEMS, null, foodItemValues);
        }

        db.close();
        return true;
    }

    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(orderId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Ensure the columns exist
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            int totalPriceIndex = cursor.getColumnIndex(COLUMN_TOTAL_PRICE);
            int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
            int orderDateIndex = cursor.getColumnIndex(COLUMN_ORDER_DATE);
            int deliveryAddressIndex = cursor.getColumnIndex(COLUMN_DELIVERY_ADDRESS);

            if (userIdIndex == -1 || totalPriceIndex == -1 || statusIndex == -1 || orderDateIndex == -1 || deliveryAddressIndex == -1) {
                Log.e("OrderDatabaseHelper", "Missing columns in orders table.");
                cursor.close();
                return null; // Return null if any required column is missing
            }

            // Retrieve order details
            int userId = cursor.getInt(userIdIndex);
            double totalPrice = cursor.getDouble(totalPriceIndex);
            String status = cursor.getString(statusIndex);
            String orderDate = cursor.getString(orderDateIndex);
            String deliveryAddress = cursor.getString(deliveryAddressIndex);

            // Retrieve food items for this order
            List<FoodCart> foodItems = new ArrayList<>();
            Cursor foodCursor = db.query(TABLE_FOOD_ITEMS, null, COLUMN_ORDER_ID_FOOD + " = ?",
                    new String[]{String.valueOf(orderId)}, null, null, null);

            if (foodCursor != null) {
                // Log food cursor row count for debugging
                Log.d("Food Cursor Row Count", String.valueOf(foodCursor.getCount()));

                while (foodCursor.moveToNext()) {
                    int foodNameIndex = foodCursor.getColumnIndex(COLUMN_FOOD_NAME);
                    int foodQuantityIndex = foodCursor.getColumnIndex(COLUMN_FOOD_QUANTITY);

                    if (foodNameIndex == -1 || foodQuantityIndex == -1) {
                        Log.e("Food Cursor Error", "Missing columns in food_items table.");
                        break; // Stop processing if the columns are not found
                    }

                    String foodName = foodCursor.getString(foodNameIndex);
                    int foodQuantity = foodCursor.getInt(foodQuantityIndex);
                    foodItems.add(new FoodCart(foodName, foodQuantity));
                }
                foodCursor.close();
            }

            // Close the order cursor
            cursor.close();

            // Return the populated Order object
            return new Order(orderId, userId, foodItems, totalPrice, status, orderDate, deliveryAddress);
        }

        cursor.close();
        return null;  // Return null if order is not found
    }
}
