package com.example.northwind;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoClients;

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
//        arrayList.add("Tao");
//        arrayList.add("Zhe");
//        arrayList.add("legend");
//        arrayList.add("football");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        ListView listView=findViewById(R.id.listview);

        app.currentUser().logOutAsync(logout->{
            Credentials credentials=Credentials.emailPassword("wongtaozhelgd@gmail.com","taozhe");
            app.loginAsync(credentials, new App.Callback<User>(){
                @Override
                public void onResult(App.Result<User> result) {
                    if(result.isSuccess()){
                        user=app.currentUser();
                        mongoClient = user.getMongoClient("mongodb-atlas");
                        mongoDatabase=mongoClient.getDatabase("northwind");
                        mongoCollection=mongoDatabase.getCollection("customers");
                        Document queryFilter=new Document().append("id",4);
//                        mongoCollection.findOne(queryFilter).getAsync(result1 -> {
//                            if(result1.isSuccess()){
//                                Log.d("aaa","Retrieve success");
//                                Log.d("aaa", String.valueOf(result1.get().getInteger("id")));
//                            }
//                            else{
//                                Log.d("aaa","Retrieve error");
//                                Log.d("aaa",result1.getError().toString());
//                            }
//                        });
                        RealmResultTask<MongoCursor<Document>> findTask=mongoCollection.find(queryFilter).iterator();
                        findTask.getAsync(task->{
                            if(task.isSuccess()){
                                MongoCursor<Document> results=task.get();
                                while(results.hasNext()){
                                    Document currentDoc=results.next();
                                    if(currentDoc.getString("name")!=null){
//                                        Log.d("aaa",currentDoc.toString());
                                        Log.d("aaa",currentDoc.getString("name"));
                                        arrayList.add(currentDoc.getString("name"));
                                    }
                                }
                                try{
                                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(Order.this, android.R.layout.simple_list_item_1,arrayList);
                                    listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList));
                                }catch (Exception e){
                                    Log.d("aaa",e.toString());
                                }

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
                        Log.d("aaa","Login error");
                        Log.d("aaa",result.toString());
                    }
                }
            });
        });

    }

}
