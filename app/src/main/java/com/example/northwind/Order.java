package com.example.northwind;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Order extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        ListView listView;
        ArrayList<String> arrayList=new ArrayList<String>();
        arrayList.add("Tao");
        arrayList.add("Zhe");
        arrayList.add("legend");
        arrayList.add("football");
        listView=findViewById(R.id.listview);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(Order.this,"selected"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
//            }
//        });

    }

}
