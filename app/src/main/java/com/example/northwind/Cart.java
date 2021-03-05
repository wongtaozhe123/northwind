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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.Document;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONException;
import org.json.JSONObject;


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



public class Cart extends AppCompatActivity implements PaymentResultListener {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        final double[] sum = {0};


        Realm.init(this);
        App app=new App(new AppConfiguration.Builder("northwind-noimz").build());
        ArrayList<String> arrayList=new ArrayList<>();
        ArrayList<Integer> arrayList1=new ArrayList();
        ArrayList<String> userInfo=new ArrayList<>();
        ArrayList<String> action=new ArrayList<>();

        String username=getIntent().getStringExtra("username");
        String password=getIntent().getStringExtra("password");
        ListView cartListView=findViewById(R.id.cartListView);
        ProgressBar progressBarCart=findViewById(R.id.progressBarCart);
        Button historyButton=findViewById(R.id.history);
        TextView totaltxt=findViewById(R.id.totalSum);
        Button toPayment=findViewById(R.id.toPayment);


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
                    Document queryFilter=new Document().append("username",username).append("status","unpaid");

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
                                    arrayList1.add(currentDoc.getInteger("price"));
                                    sum[0] +=currentDoc.getInteger("price");
                                }
                            }
                            try{
                                ProgramAdapter programAdapter=new ProgramAdapter(Cart.this,arrayList,arrayList1,userInfo,action);
                                cartListView.setAdapter(programAdapter);
                                totaltxt.setText("total: RM"+sum[0]);
                                progressBarCart.setVisibility(View.INVISIBLE);
                            }catch (Exception e){
                                Log.d("aaa",e.toString());
                            }
                        }
                        else{
                            Log.d("aaa",task.getError().toString());
                            Toast.makeText(Cart.this,task.getError().toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(Cart.this,result.getError().toString(),Toast.LENGTH_SHORT).show();
                }
                progressBarCart.setVisibility(View.INVISIBLE);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),History.class);
                i.putExtra("username", username.toString());
                i.putExtra("password",password.toString());
                startActivity(i);
                finish();
            }
        });

        toPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAmount = String.valueOf(sum[0]);
                int amount = Math.round(Float.parseFloat(sAmount)*100);
                //Initialize razorpay checkout
                Checkout checkout = new Checkout();
                //Set key id
                checkout.setKeyID("rzp_test_ZWEEVpK7gsjGGu");
                //set image

                //initialize json object
                JSONObject object = new JSONObject();
                try{
                    //put name
                    object.put("name", "Android Coding");
                    //put description
                    object.put("description", "Test Payment");
                    //put theme color
                    object.put("theme.color", "#0093DD");
                    //put currency unit
                    object.put("currency", "INR");
                    //put amount
                    object.put("amount", amount);
                    //put mobile number
                    object.put("prefill.contact", "9876543210");
                    //put email
                    object.put("prefill.email", "androidcoding@rzp.com");
                    //open razorpay checkout activity
                    checkout.open(Cart.this,object);
                }catch(JSONException e){
                    e.printStackTrace();
                }
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

    @Override
    public void onPaymentSuccess(String s) {
        //initialize aleart dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Payment ID");
        //set message
        builder.setMessage(s);
        //show alert dialog
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        //display toast
        Toast.makeText(getApplicationContext()
                ,s, Toast.LENGTH_SHORT).show();
    }
}
