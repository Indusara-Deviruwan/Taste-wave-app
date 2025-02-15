package com.example.tastewaveapp.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tastewaveapp.model.ReviewRating;

import java.util.ArrayList;
import java.util.List;

public class ReviewRatingDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "review_rating.db";
    private static final int DATABASE_VERSION = 2;

    // Table Name
    private static final String TABLE_REVIEW_RATING = "review_rating";

    // Column Names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RESTAURANT_ID = "restaurant_id";
    private static final String COLUMN_RESTAURANT_NAME = "restaurant_name";
    private static final String COLUMN_FOOD_ID = "food_id";
    private static final String COLUMN_FOOD_NAME = "food_name";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_REVIEW_TEXT = "review_text";
    private static final String COLUMN_PHOTO_URI = "photo_uri";

    // Table Create Statement
    private static final String CREATE_TABLE_REVIEW_RATING = "CREATE TABLE " + TABLE_REVIEW_RATING + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RESTAURANT_ID + " TEXT, "
            + COLUMN_RESTAURANT_NAME + " TEXT, "
            + COLUMN_FOOD_ID + " TEXT, "
            + COLUMN_FOOD_NAME + " TEXT, "
            + COLUMN_RATING + " REAL, "
            + COLUMN_REVIEW_TEXT + " TEXT, "
            + COLUMN_PHOTO_URI + " TEXT" + ")";

    public ReviewRatingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_REVIEW_RATING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW_RATING);
        onCreate(db);
    }

    // Insert a new ReviewRating
    public long addReviewRating(ReviewRating reviewRating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_RESTAURANT_ID, reviewRating.getRestaurantId());
        values.put(COLUMN_RESTAURANT_NAME, reviewRating.getRestaurantName());
        values.put(COLUMN_FOOD_ID, reviewRating.getFoodId());
        values.put(COLUMN_FOOD_NAME, reviewRating.getFoodName());
        values.put(COLUMN_RATING, reviewRating.getRating());
        values.put(COLUMN_REVIEW_TEXT, reviewRating.getReviewText());
        values.put(COLUMN_PHOTO_URI, reviewRating.getPhotoUri());

        long id = db.insert(TABLE_REVIEW_RATING, null, values);
        db.close();
        return id;
    }

    // Get a single ReviewRating by ID
    public ReviewRating getReviewRating(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REVIEW_RATING, new String[]{COLUMN_ID, COLUMN_RESTAURANT_ID, COLUMN_RESTAURANT_NAME, COLUMN_FOOD_ID, COLUMN_FOOD_NAME, COLUMN_RATING, COLUMN_REVIEW_TEXT, COLUMN_PHOTO_URI},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ReviewRating reviewRating = new ReviewRating(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_TEXT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_URI))
        );

        cursor.close();
        return reviewRating;
    }

    // Get all ReviewRatings
    public List<ReviewRating> getAllReviewRatings() {
        List<ReviewRating> reviewRatingList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REVIEW_RATING;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ReviewRating reviewRating = new ReviewRating(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_URI))
                );
                reviewRatingList.add(reviewRating);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return reviewRatingList;
    }

    // Delete a ReviewRating
    public void deleteReviewRating(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REVIEW_RATING, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
