package com.example.northwind;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Cart extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        //

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.order);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.order:
                        startActivity(new Intent(getApplicationContext(), ProductChoice.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart:
                        return true;
                }
                return false;
            }
        });
    }
}
