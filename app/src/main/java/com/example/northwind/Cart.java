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

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;


import java.lang.reflect.Array;
import java.text.DecimalFormat;
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

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.currentDate;
import static com.mongodb.client.model.Updates.set;


public class Cart extends AppCompatActivity implements PaymentResultListener {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    User user;
    String strings;
    ArrayList<String> st=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        final double[] sum = {0};
        DecimalFormat df2 = new DecimalFormat("#.##");


        Realm.init(this);
        App app=new App(new AppConfiguration.Builder("northwind-noimz").build());
        ArrayList<String> arrayList=new ArrayList<>();
        ArrayList<Double> arrayList1=new ArrayList();
        ArrayList<String> userInfo=new ArrayList<>();
        ArrayList<String> action=new ArrayList<>();
        int[] foodImg = {
                R.drawable.food,R.drawable.food2,R.drawable.food3,R.drawable.food4,R.drawable.food5,R.drawable.food6,R.drawable.food7,R.drawable.food8
        };

        String username=getIntent().getStringExtra("username");
        String password=getIntent().getStringExtra("password");
        ListView cartListView=findViewById(R.id.cartListView);
        ProgressBar progressBarCart=findViewById(R.id.progressBarCart);
        Button historyButton=findViewById(R.id.history);
        Button cartButton=findViewById(R.id.unpaid);
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
                                    arrayList1.add(currentDoc.getDouble("price"));
                                    sum[0] +=currentDoc.getDouble("price");
                                }
                            }
                            try{
                                ProgramAdapter programAdapter=new ProgramAdapter(Cart.this,arrayList,arrayList1,userInfo,action,foodImg);
                                cartListView.setAdapter(programAdapter);
                                totaltxt.setText("total: RM"+df2.format(sum[0]));
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

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Cart.class);
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
        testRealm();
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

    public void updateCart() {
        //update status unpaid to paid
        App app=new App(new AppConfiguration.Builder("northwind-noimz").build());
        Credentials credentials=Credentials.emailPassword("wongtaozhelgd@gmail.com","taozhe");
        app.loginAsync(credentials, new App.Callback<User>(){
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    String username=getIntent().getStringExtra("username");
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase=mongoClient.getDatabase("northwind");
                    MongoCollection<Document> collection = mongoDatabase.getCollection("cart");
                    collection.updateMany(eq("username", username), combine(set("status", "paid"), currentDate("lastModified")));
                    Toast.makeText(Cart.this, username.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Cart.this,result.getError().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*public void realmUpdateCart() {
        Realm.init(this);
        String username = getIntent().getStringExtra("username");
        Document queryFilter = new Document().append("username", username).append("status", "unpaid");
        mongoCollection=mongoDatabase.getCollection("cart");
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if(task.isSuccess())
            {
                MongoCursor<Document> results = task.get();

                if(results.hasNext())
                {
                    Log.v("FindFunction", "Found Something");
                    Document result = results.next();
                    strings = (ArrayList<String>)result.get("status");
                    if(strings == null)
                    {
                          = new ArrayList<>();
                    }
                    String data = "paid";
                    strings.add(data);
                    result.append("status",strings);
                    mongoCollection.updateOne(queryFilter, result).getAsync(result1 -> {
                        if(result1.isSuccess())
                        {
                            Log.v("UpdateFunction", "Updated Data");
                        }
                        else
                        {
                            Log.v("UpdatedFunction", "Error"+result1.getError().toString());
                        }
                    });
                }
                else
                {
                    Log.v("FindFunction", "Found Nothing");
                }
            }
            else
            {
                Log.v("Error",task.getError().toString());
            }
        });
    }*/

    public void testRealm() {
        ProgressBar progressBarCart=findViewById(R.id.progressBarCart);
        String username=getIntent().getStringExtra("username");
        String password=getIntent().getStringExtra("password");
        App app=new App(new AppConfiguration.Builder("northwind-noimz").build());
        Credentials credentials=Credentials.emailPassword("wongtaozhelgd@gmail.com","taozhe");
        app.loginAsync(credentials, new App.Callback<User>(){
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    progressBarCart.setVisibility(View.VISIBLE);
                    user=app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase=mongoClient.getDatabase("northwind");
                    String username = getIntent().getStringExtra("username");
                    Document queryFilter = new Document().append("username", username).append("status", "unpaid");
                    mongoCollection=mongoDatabase.getCollection("cart");
                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                    findTask.getAsync(task -> {
                        if(task.isSuccess())
                        {
                            MongoCursor<Document> results = task.get();
                            while(results.hasNext())
                            {
                                Log.v("FindFunction", "Found Something");
                                Document resulta = results.next();
                                resulta.append("status","paid");
                                Log.d("Update",ObjectId.get().toString());
                                    mongoCollection.updateOne(queryFilter,resulta).getAsync(result1 -> {
                                        if(result1.isSuccess())
                                        {
                                            Log.v("UpdateFunction", "Updated Data");
                                        }
                                        else
                                        {
                                            Log.v("UpdateFunction", "Error"+result1.getError().toString());
                                        }
                                    });
                            }
                            progressBarCart.setVisibility(View.INVISIBLE);
                            Intent i=new Intent(getApplicationContext(),History.class);
                            i.putExtra("username",username);
                            i.putExtra("password",password);
                            startActivity(i);
                            finish();
//                            else
//                            {
//                                Log.v("FindFunction", "Found Nothing");
//                            }
                        }
                        else
                        {
                            Log.v("Error",task.getError().toString());
                        }

                    });
                }else{
                    Toast.makeText(Cart.this,result.getError().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
