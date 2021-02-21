package com.example.northwind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProgramAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> foodName;
    ArrayList<Integer> foodPrice;

    public ProgramAdapter(Context context, ArrayList<String> foodName, ArrayList<Integer> foodPrice) {
        super(context, R.layout.single_item,R.id.foodName,foodName);
        this.context=context;
        this.foodName=foodName;
        this.foodPrice=foodPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleItem=convertView;
        ProgramViewHolder holder=null;
        if(singleItem==null){
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.single_item,parent,false);
            holder=new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        }
        else{
            holder= (ProgramViewHolder) singleItem.getTag();
        }
        holder.foodName.setText("~ "+foodName.get(position)+" ~");
        holder.foodPrice.setText("~ RM"+ foodPrice.get(position) +" ~");
        singleItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"YOu choosed "+foodName.get(position),Toast.LENGTH_SHORT).show();
            }
        });
        return singleItem;
    }
}
