package com.example.northwind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_choice);
        CardView card1=findViewById(R.id.card1);
        CardView card2=findViewById(R.id.card2);
        CardView card3=findViewById(R.id.card3);
        CardView card4=findViewById(R.id.card4);
        CardView card5=findViewById(R.id.card5);
        CardView card6=findViewById(R.id.card6);
        CardView card7=findViewById(R.id.card7);
        CardView card8=findViewById(R.id.card8);

        String username=getIntent().getStringExtra("username");
        String password=getIntent().getStringExtra("password");

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",1);
                i.putExtra("username",username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",2);
                i.putExtra("username",username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",3);
                i.putExtra("username",username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",4);
                i.putExtra("username",username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",5);
                i.putExtra("username",username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",6);
                i.putExtra("username",username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
            }
        });
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",7);
                i.putExtra("username",username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
            }
        });
        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",8);
                i.putExtra("username",username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
            }
        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.order);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.order:
                        return true;
                    case R.id.cart:
                        Intent i = new Intent(getApplicationContext(), Cart.class);
                        i.putExtra("username", username.toString());
                        i.putExtra("password",password.toString());
                        startActivity(i);
                        overridePendingTransition(0,0);

                        return true;
                }
                return false;
            }
        });
    }
}
