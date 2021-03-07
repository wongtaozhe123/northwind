package com.example.northwind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ProgramAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> foodName;
    ArrayList<Double> foodPrice;
    ArrayList<String> userInfo;
    ArrayList<String> action;
    ArrayList<String> strings;
    int temp=0;
    public ProgramAdapter(Context context, ArrayList<String> foodName, ArrayList<Double> foodPrice, ArrayList<String> userInfo, ArrayList<String> action) {
        super(context, R.layout.single_item,R.id.foodName,foodName);
        this.context=context;
        this.foodName=foodName;
        this.foodPrice=foodPrice;
        this.userInfo=userInfo;
        this.action=action;
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
//                Realm.init(this);
                App app=new App(new AppConfiguration.Builder("northwind-noimz").build());
                Credentials credentials=Credentials.emailPassword("wongtaozhelgd@gmail.com","taozhe");
                app.loginAsync(credentials, new App.Callback<User>(){
                    @Override
                    public void onResult(App.Result<User> result) {
                        if(result.isSuccess()){
                            if(action.get(0)=="add"){
                                Toast.makeText(getContext(),"Adding "+foodName.get(position)+" to cart",Toast.LENGTH_SHORT).show();
                                User user =app.currentUser();
                                MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
                                MongoDatabase mongoDatabase=mongoClient.getDatabase("northwind");
                                MongoCollection<Document> mongoCollection=mongoDatabase.getCollection("cart");
                                mongoCollection.insertOne(new Document("userId",user.getId()).append("username",userInfo.get(0))
                                        .append("food",foodName.get(position)).append("price",foodPrice.get(position))
                                        .append("status","unpaid")).getAsync(result1 -> {
                                    if(result1.isSuccess()){
                                        Toast.makeText(getContext(),foodName.get(position)+" added to cart!!",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Log.d("aaa",result1.getError().toString());
                                        Toast.makeText(getContext(),result1.getError().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else if(action.get(0)=="remove"){
                                Toast.makeText(getContext(),"Removing "+foodName.get(position)+" from cart",Toast.LENGTH_SHORT).show();
                                User user =app.currentUser();
                                MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
                                MongoDatabase mongoDatabase=mongoClient.getDatabase("northwind");
                                MongoCollection<Document> mongoCollection=mongoDatabase.getCollection("cart");
                                Document document=new Document().append("username",userInfo.get(0)).append("food",foodName.get(position)).append("status","unpaid");

                                mongoCollection.deleteOne(document).getAsync(task -> {
                                    if(task.isSuccess()){
                                        Toast.makeText(getContext(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context.getApplicationContext(), Cart.class);
                                        i.putExtra("username", userInfo.get(0));
                                        i.putExtra("password",userInfo.get(1));
                                        context.startActivity(i);
//                                        ((Activity) context.getApplicationContext()).recreate();
//                                        ((Activity) context.getApplicationContext()).finish();
                                    }
                                    else{
                                        Log.d("aaa",task.getError().toString());
                                    }
                                });

                            }
                        }
                        else {
                            //Toast.makeText(getContext(),foodName.get(position)+" added to cart!!",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(),"An error occur please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        return singleItem;
    }
}