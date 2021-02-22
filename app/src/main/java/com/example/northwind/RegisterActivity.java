package com.example.northwind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class RegisterActivity extends AppCompatActivity {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection mongoCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText username =findViewById(R.id.input_username);
        EditText password=findViewById(R.id.input_password);
        Button btn_Register=findViewById(R.id.btn_Register);
        Button btn_backToLogin = findViewById(R.id.btn_backToLogin);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        App app = new App(new AppConfiguration.Builder("northwind-noimz").build());
        btn_Register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                app.getEmailPassword().registerUserAsync(username.getText().toString(), password.getText().toString(), it->{
                    if(username.getText() == null || password.getText() == null){
                        Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_LONG).show();
                        Log.d("aaa","Registered failed");
                    }
                    if(it.isSuccess()){
                        Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
                        Log.d("aaa","Registered successful");
                        Intent i= new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("username", username.getText().toString());
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_LONG).show();
                        Log.d("aaa","Registered failed");
                    }
                });

            }
        });

        btn_backToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });
    }
}
