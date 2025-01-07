package com.example.tastewaveapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastewaveapp.R;
import com.example.tastewaveapp.adapter.RestaurantAdapter;
import com.example.tastewaveapp.model.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list of restaurants
        restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant("Restaurant 1", "A great place for pizza and pasta.", R.drawable.start));
        restaurantList.add(new Restaurant("Restaurant 2", "Known for its delicious sushi and seafood.", R.drawable.start));
        restaurantList.add(new Restaurant("Restaurant 3", "A popular spot for burgers and fries.", R.drawable.start));

        // Set up the adapter
        restaurantAdapter = new RestaurantAdapter(restaurantList);
        recyclerView.setAdapter(restaurantAdapter);
    }
}

