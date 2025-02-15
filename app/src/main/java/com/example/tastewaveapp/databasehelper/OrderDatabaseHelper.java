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
    private static final int DATABASE_VERSION = 10;

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ID = "order_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TOTAL_PRICE = "total_price";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_DELIVERY_ADDRESS = "delivery_address";

    public static final String TABLE_FOOD_ITEMS = "food_items";
    public static final String COLUMN_FOOD_ITEM_ID = "food_item_id";
    public static final String COLUMN_ORDER_ID_FOOD = "order_id";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_FOOD_QUANTITY = "food_quantity";
    public static final String COLUMN_FOOD_RESTAURANT_ID = "restaurant_id";
    public static final String COLUMN_FOOD_RESTAURANT_NAME = "restaurant_name";

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
                COLUMN_FOOD_RESTAURANT_ID + " INTEGER, " +
                COLUMN_FOOD_RESTAURANT_NAME + " TEXT, " +
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

    public long insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, order.getUserId());
        values.put(COLUMN_TOTAL_PRICE, order.getTotalPrice());
        values.put(COLUMN_STATUS, order.getOrderStatus());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_DELIVERY_ADDRESS, order.getDeliveryAddress());

        long orderId = db.insert(TABLE_ORDERS, null, values); // Insert and get the generated orderId

        if (orderId == -1) {
            db.close();
            return -1;  // Order insertion failed
        }

        // Insert each food item associated with the order
        for (FoodCart foodItem : order.getFoodItems()) {
            ContentValues foodItemValues = new ContentValues();
            foodItemValues.put(COLUMN_ORDER_ID_FOOD, orderId);
            foodItemValues.put(COLUMN_FOOD_NAME, foodItem.getName());
            foodItemValues.put(COLUMN_FOOD_QUANTITY, foodItem.getQuantity());
            foodItemValues.put(COLUMN_FOOD_RESTAURANT_ID, foodItem.getRestaurantId());
            foodItemValues.put(COLUMN_FOOD_RESTAURANT_NAME, foodItem.getRestaurantName());

            long foodResult = db.insert(TABLE_FOOD_ITEMS, null, foodItemValues);
            if (foodResult == -1) {
                Log.e("OrderDatabaseHelper", "Failed to insert food item: " + foodItem.getName());
            }
        }

        db.close();
        return orderId;  // Return the generated orderId
    }


    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(orderId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
            String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
            String deliveryAddress = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DELIVERY_ADDRESS));

            List<FoodCart> foodItems = new ArrayList<>();
            Cursor foodCursor = db.query(TABLE_FOOD_ITEMS, null, COLUMN_ORDER_ID_FOOD + " = ?",
                    new String[]{String.valueOf(orderId)}, null, null, null);

            if (foodCursor != null) {
                while (foodCursor.moveToNext()) {
                    String foodName = foodCursor.getString(foodCursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME));
                    int foodQuantity = foodCursor.getInt(foodCursor.getColumnIndexOrThrow(COLUMN_FOOD_QUANTITY));
                    int foodRestaurantId = foodCursor.getInt(foodCursor.getColumnIndexOrThrow(COLUMN_FOOD_RESTAURANT_ID));
                    String foodRestaurantName = foodCursor.getString(foodCursor.getColumnIndexOrThrow(COLUMN_FOOD_RESTAURANT_NAME));
                    foodItems.add(new FoodCart(foodName, foodQuantity, foodRestaurantId, foodRestaurantName));
                }
                foodCursor.close();
            }

            cursor.close();
            return new Order(orderId, userId, foodItems, totalPrice, status, orderDate, deliveryAddress);
        }

        if (cursor != null) cursor.close();
        return null;
    }
}
