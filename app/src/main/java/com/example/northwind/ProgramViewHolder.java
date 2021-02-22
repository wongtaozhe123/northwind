package com.example.northwind;

import android.view.View;
import android.widget.TextView;

public class ProgramViewHolder {

    TextView foodName;
    TextView foodPrice;
    ProgramViewHolder(View v){
        foodName=v.findViewById(R.id.foodName);
        foodPrice=v.findViewById(R.id.foodPrice);
    }

}

