package com.example.northwind;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import org.bson.Document;

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

public class Order extends AppCompatActivity{
    String Appid="northwind-noimz";
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this);
        App app=new App(new AppConfiguration.Builder(Appid).build());
        ArrayList<String> arrayList=new ArrayList<>();
        ArrayList<Double> arrayList1=new ArrayList();
        ArrayList<String> userInfo=new ArrayList<>();
        ArrayList<String> action=new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        ListView listView=findViewById(R.id.listview);
        ProgressBar progressBar=findViewById(R.id.progressBar);

        int category=getIntent().getIntExtra("category",0);
        String username=getIntent().getStringExtra("username");
        String password=getIntent().getStringExtra("password");

            Credentials credentials=Credentials.emailPassword("wongtaozhelgd@gmail.com","taozhe");
            app.loginAsync(credentials, new App.Callback<User>(){
                @Override
                public void onResult(App.Result<User> result) {
                    if(result.isSuccess()){
                        progressBar.setVisibility(View.VISIBLE);
                        user=app.currentUser();
                        mongoClient = user.getMongoClient("mongodb-atlas");
                        mongoDatabase=mongoClient.getDatabase("northwind");
                        mongoCollection=mongoDatabase.getCollection("products");
                        Document queryFilter=new Document().append("categoryID",category);

                        RealmResultTask<MongoCursor<Document>> findTask=mongoCollection.find(queryFilter).iterator();
                        findTask.getAsync(task->{
                            if(task.isSuccess()){
                                MongoCursor<Document> results=task.get();
                                userInfo.add(username.toString());
                                userInfo.add(password.toString());
                                action.add("add");
                                while(results.hasNext()){
                                    Document currentDoc=results.next();

                                    if(currentDoc.getString("productName")!=null){
//                                        Log.d("aaa",currentDoc.toString());
                                        arrayList.add(currentDoc.getString("productName"));
                                        arrayList1.add(currentDoc.getDouble("unitPrice"));
                                    }
                                }
                                try{
//                                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(Order.this, android.R.layout.simple_list_item_1,arrayList);
//                                    listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList));
                                    ProgramAdapter programAdapter=new ProgramAdapter(Order.this,arrayList,arrayList1,userInfo,action);
                                    listView.setAdapter(programAdapter);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }catch (Exception e){
                                    Log.d("aaa",e.toString());
                                }
//                                progressBar.setVisibility(View.INVISIBLE);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Toast.makeText(Order.this,"selected"+(i+1)+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                Log.d("aaa",task.getError().toString());
                            }
                        });
                    }
                    else{
                        Log.d("aaa",result.toString());
                        Toast.makeText(Order.this,result.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

}
