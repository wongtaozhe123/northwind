package com.example.northwind;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;

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
        TextView registerText = findViewById(R.id.txt_register);

        SpannableString ss = new SpannableString(registerText.getText().toString());
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        };

        ss.setSpan(span1, 23, 36 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerText.setText(ss);
        registerText.setMovementMethod(LinkMovementMethod.getInstance());


        Intent intent = getIntent();
        String r_username = intent.getStringExtra("username");
        if (r_username != null) {
            username.setText(r_username);
        }
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        App app = new App(new AppConfiguration.Builder("northwind-noimz").build());

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText() != null && password.getText() != null) {
                    Toast.makeText(getApplicationContext(), "Please fill in your email and password !", Toast.LENGTH_LONG).show();
                    Log.d("aaa", "Login failed");
                } else {
                    Credentials credentials = Credentials.emailPassword(String.valueOf(username.getText()), String.valueOf(password.getText()));
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
                            } else {
                                Toast.makeText(getApplicationContext(), "Login failed ! Please provide correct username with password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}