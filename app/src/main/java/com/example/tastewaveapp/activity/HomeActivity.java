package com.example.tastewaveapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.activity.RestaurantActivity;
import com.example.tastewaveapp.adapter.RestaurantAdapter;
import com.example.tastewaveapp.databasehelper.DatabaseHelper;
import com.example.tastewaveapp.model.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView textView4;
    private Button buttonSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.toggle_button).setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
            popupMenu.getMenuInflater().inflate(R.menu.toolbar_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int id = menuItem.getItemId();
                if (id == R.id.action_messages) {
                    Toast.makeText(this, "Orders Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_notifications) {
                    Toast.makeText(this, "Offers Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_profile) {
                    Toast.makeText(this, "Favorites Selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });


        textView4 = findViewById(R.id.textViewWelcome);
        buttonSignOut = findViewById(R.id.buttonSignOut);

        //check if the user already signed in
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            String show = "click here to log-in";
            textView4.setText(show);
        }
        else {
            textView4.setText(user.getEmail());
        }

        // Set up button click listeners
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sign-up logic (this is just a placeholder)
                Toast.makeText(HomeActivity.this, "You are Signed out", Toast.LENGTH_SHORT).show();

                // Navigate to Log-in Activity
                FirebaseAuth.getInstance().signOut();
                String show = "click here to log-in";
                textView4.setText(show);
            }
        });
/*
        ImageButton cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(v -> {
            // Navigate to CartActivity
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

*/
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            // Pass the user ID to ProfileActivity

            if (id == R.id.nav_profile) {
                // Navigate to Profile Activity
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));

                return true;
            } else if (id == R.id.nav_orders) {
                // Navigate to Orders Activity
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
                return true;
            } else if (id == R.id.nav_payment) {
                // Navigate to Payment Activity
                startActivity(new Intent(HomeActivity.this, PaymentActivity.class));
                return true;
            } else if (id == R.id.nav_offers) {
                // Navigate to Offers Activity
                startActivity(new Intent(HomeActivity.this, OffersActivity.class));
                return true;
            }
            return false;
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        //databaseHelper.insertRestaurant("MR.Kottu","good kottu", R.drawable.start);

        // Fetch restaurants from the database
        restaurantList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllRestaurants();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESTAURANT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESTAURANT_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESTAURANT_DESCRIPTION));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESTAURANT_IMAGE));
                restaurantList.add(new Restaurant(id, name, description, imageResId));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Set up the adapter
        restaurantAdapter = new RestaurantAdapter(restaurantList, restaurant -> {
            // Handle restaurant click
            Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
            intent.putExtra("restaurant_id", restaurant.getId());
            intent.putExtra("restaurant_name", restaurant.getName());
            intent.putExtra("restaurant_description", restaurant.getDescription());
            intent.putExtra("restaurant_image", restaurant.getImageResId());
            startActivity(intent);
        });

        recyclerView.setAdapter(restaurantAdapter);


    }
}
