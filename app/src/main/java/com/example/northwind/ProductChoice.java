package com.example.northwind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",1);
                startActivity(i);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",2);
                startActivity(i);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",3);
                startActivity(i);
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",4);
                startActivity(i);
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",5);
                startActivity(i);
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",6);
                startActivity(i);
            }
        });
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",7);
                startActivity(i);
            }
        });
        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                i.putExtra("category",8);
                startActivity(i);
            }
        });
    }
}
