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
        EditText username = findViewById(R.id.input_username);
        EditText password = findViewById(R.id.input_password);
        Button btn_Register = findViewById(R.id.btn_Register);
        TextView txt_backToLogin = findViewById(R.id.txt_backToLogin);

        SpannableString ss = new SpannableString(txt_backToLogin.getText().toString());
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        };

        ss.setSpan(span1, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_backToLogin.setText(ss);
        txt_backToLogin.setMovementMethod(LinkMovementMethod.getInstance());

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        App app = new App(new AppConfiguration.Builder("northwind-noimz").build());
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.getEmailPassword().registerUserAsync(username.getText().toString(), password.getText().toString(), it -> {
                    if (username.getText() == null || password.getText() == null) {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                        Log.d("aaa", "Registered failed");
                    }
                    if (it.isSuccess()) {
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                        Log.d("aaa", "Registered successful");
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("username", username.getText().toString());
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                        Log.d("aaa", "Registered failed");
                    }
                });

            }
        });

    }
}
