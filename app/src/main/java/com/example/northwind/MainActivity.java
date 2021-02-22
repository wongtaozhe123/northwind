package com.example.northwind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection mongoCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText username = findViewById(R.id.input_ID);
        EditText password = findViewById(R.id.input_PW);
        Button button2 = findViewById(R.id.btn_login);
        Button buttonN = findViewById(R.id.navigate);
        ArrayList<String> strings = new ArrayList<>();
        Intent intent = getIntent();
        String r_username = intent.getStringExtra("username");
        if (r_username != null) {
            username.setText(r_username);
        }
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        App app = new App(new AppConfiguration.Builder("northwind-noimz").build());
        buttonN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Credentials credentials = Credentials.emailPassword(String.valueOf(username.getText()), String.valueOf(password.getText()));
//                username.setText("Zchuen");
//                password.setText("12345");
                app.loginAsync(credentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> result) {
                        if (result.isSuccess()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                            Log.d("aaa", "Login successful");
                            User user = app.currentUser();
                            Log.d("User", String.valueOf(user));
                            mongoClient = user.getMongoClient("mongodb-atlas");
                            mongoDatabase = mongoClient.getDatabase("northwind");
                            mongoCollection = mongoDatabase.getCollection("customers");
                            Document document = new Document("userId", user.getId());
                            document.append("id", 5);
                            document.append("name", "Hi");
                            Intent i = new Intent(getApplicationContext(), ProductChoice.class);
                            i.putExtra("username", username.toString());
                            startActivity(i);

//                            mongoCollection.insertOne(document).getAsync(result1 -> {
//                                if(result1.isSuccess()){
//                                    Log.d("aaa","Data inserted?");
//                                }
//                                else{
//                                    Log.d("aaa","Failed to insert lol");
//                                    Log.d("aaa",result1.getError().toString());
//                                }
//                            });
                            Document queryFilter = new Document().append("name", "Myth");
//                            mongoCollection.findOne(queryFilter).getAsync(result1 -> {
//                                if(result1.isSuccess()){
//                                    Log.d("aaa","found it");
//                                    Toast.makeText(getApplicationContext(),"Found",Toast.LENGTH_LONG).show();
//                                    Document resultd= (Document) result1.get();
//                                    Log.d("aaa", resultd.getString("name"));
//                               }
//                                else{
//                                    Log.d("aaa","can't find");
//                                    Log.d("aaa",result1.getError().toString());
//                                }
//                           });

                        } else {
                            Document queryFilter = new Document().append("name", "Myth");
                            Toast.makeText(getApplicationContext(), "Login failed ! Please provide correct username with password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}