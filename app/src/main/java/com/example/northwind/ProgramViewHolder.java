package com.example.northwind;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramViewHolder {

    ImageView foodImage;
    TextView foodName;
    TextView foodPrice;
    ProgramViewHolder(View v){
        foodName=v.findViewById(R.id.foodName);
        foodPrice=v.findViewById(R.id.foodPrice);
        foodImage=v.findViewById(R.id.foodImage);
    }

}

