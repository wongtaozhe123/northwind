 package com.example.northwind;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.bson.Document;

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
import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MainActivity extends AppCompatActivity {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection mongoCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText num1=findViewById(R.id.num1);
        EditText num2=findViewById(R.id.num2);
        TextView name=findViewById(R.id.name);
        Button button2=findViewById(R.id.button2);
        Button buttonN=findViewById(R.id.navigate);
        ArrayList<String> strings=new ArrayList<>();

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        App app = new App(new AppConfiguration.Builder("northwind-noimz").build());
        buttonN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Order.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Credentials credentials=Credentials.emailPassword("wongtaozhelgd@gmail.com","taozhe");
                app.loginAsync(credentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> result) {
                        if(result.isSuccess()){
                            Log.d("aaa","Login successful");
                            User user = app.currentUser();
                            mongoClient= user.getMongoClient("mongodb-atlas");
                            mongoDatabase=mongoClient.getDatabase("northwind");
                            mongoCollection=mongoDatabase.getCollection("customers");
                            Document document=new Document("userId",user.getId());
                            document.append("id",4);
                            document.append("name","Myth");
//                            mongoCollection.insertOne(document).getAsync(result1 -> {
//                                if(result1.isSuccess()){
//                                    Log.d("aaa","Data inserted?");
//                                }
//                                else{
//                                    Log.d("aaa","Failed to insert lol");
//                                    Log.d("aaa",result1.getError().toString());
//                                }
//                            });
                            Document queryFilter=new Document().append("name","Myth");
//                            mongoCollection.findOne(queryFilter).getAsync(result1 -> {
//                                if(result1.isSuccess()){
//                                    Log.d("aaa","found it");
//                                    Document resultd= (Document) result1.get();
//                                    Log.d("aaa", resultd.getString("name"));
//                                }
//                                else{
//                                    Log.d("aaa","can't find");
//                                    Log.d("aaa",result1.getError().toString());
//                                }
//                            });
                        }
                        else{
                            Log.d("aaa","failed");
                        }
                    }
                });
            }
        });

    }
}