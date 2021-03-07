package com.example.northwind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.Document;

import java.lang.reflect.Array;
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

public class History extends AppCompatActivity{
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        Realm.init(this);
        App app=new App(new AppConfiguration.Builder("northwind-noimz").build());
        ArrayList<String> arrayList=new ArrayList<>();
        ArrayList<Double> arrayList1=new ArrayList();
        ArrayList<String> userInfo=new ArrayList<>();
        ArrayList<String> action=new ArrayList<>();

        String username=getIntent().getStringExtra("username");
        String password=getIntent().getStringExtra("password");
        ListView historyListView=findViewById(R.id.historyListView);
        ProgressBar progressBarCart=findViewById(R.id.progressBarCart);
        Button unpaidButton = findViewById(R.id.unpaid);

        Credentials credentials=Credentials.emailPassword("wongtaozhelgd@gmail.com","taozhe");
        app.loginAsync(credentials, new App.Callback<User>(){
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    progressBarCart.setVisibility(View.VISIBLE);
                    user=app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase=mongoClient.getDatabase("northwind");
                    mongoCollection=mongoDatabase.getCollection("cart");
                    Document queryFilter=new Document().append("username",username).append("status","paid");

                    RealmResultTask<MongoCursor<Document>> findTask=mongoCollection.find(queryFilter).iterator();
                    findTask.getAsync(task->{
                        if(task.isSuccess()){
                            MongoCursor<Document> results=task.get();
                            userInfo.add(username.toString());
                            userInfo.add(password.toString());
                            action.add("remove");
                            while(results.hasNext()){
                                Document currentDoc=results.next();
                                if(currentDoc.getString("food")!=null){
//                                        Log.d("aaa",currentDoc.toString());
                                    arrayList.add(currentDoc.getString("food"));
                                    arrayList1.add(currentDoc.getDouble("price"));
                                }
                            }
                            try{
                                ProgramAdapter programAdapter=new ProgramAdapter(History.this,arrayList,arrayList1,userInfo,action);
                                historyListView.setAdapter(programAdapter);
                                progressBarCart.setVisibility(View.INVISIBLE);
//                                progressBar.setVisibility(View.INVISIBLE);
                            }catch (Exception e){
                                Log.d("aaa",e.toString());
                            }
                        }
                        else{
                            Log.d("aaa",task.getError().toString());
                            Toast.makeText(History.this,task.getError().toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(History.this,result.getError().toString(),Toast.LENGTH_SHORT).show();
                }
                progressBarCart.setVisibility(View.INVISIBLE);
            }
        });

        unpaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Cart.class);
                i.putExtra("username", username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.order);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.order:
                        Intent i = new Intent(getApplicationContext(), ProductChoice.class);
                        i.putExtra("username", username.toString());
                        i.putExtra("password",password.toString());
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart:
                        return true;
                }
                return false;
            }
        });
    }
}
