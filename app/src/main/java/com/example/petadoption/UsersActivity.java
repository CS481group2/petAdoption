package com.example.petadoption;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.firebase.database.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
//import android.support.v7.app.AppCompatActivity;

public class UsersActivity extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    //ArrayList<UserDetails> userClassList = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    private FirebaseUser user;
    private String localUser = "";
    private String userID;
    private String KEY_EMAIL = "email";
    private String email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //String url = "https://petadoption-e740c-default-rtdb.firebaseio.com/users.json";

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance() .collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    UserController.currentUser = new UserDetails(task.getResult().getString("firstName") + " " + task.getResult().getString("lastName"), userID);
                    Log.println(Log.ASSERT, "???", UserController.currentUser.getName());
                }
            }
        });

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                        {
                            al.add(queryDocumentSnapshot.getString("firstName") + " " + queryDocumentSnapshot.getString("lastName"));
                            UserController.userD.add(new UserDetails(al.get(totalUsers), queryDocumentSnapshot.getId()));
                            /*if (userID.equals(queryDocumentSnapshot.getId()))
                            {
                                //Log.println(Log.ASSERT, "YEAH ITS HERE", "DAWG");
                                UserController.userD.get(totalUsers).setCurrUser();
                                Log.println(Log.ASSERT, "???", UserController.getCurrUser(userID).getName());
                            }*/

                            //UserDetails.name = al.get(totalUsers);
                            totalUsers++;
                        }

                        if(totalUsers == 0){
                            noUsersText.setVisibility(View.VISIBLE);
                            usersList.setVisibility(View.GONE);
                        }
                        else{

                            noUsersText.setVisibility(View.GONE);
                            usersList.setVisibility(View.VISIBLE);
                            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
                        }
                    }
                });

        RequestQueue rQueue = Volley.newRequestQueue(UsersActivity.this);
        //rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserController.currentUser.setChatWith(al.get(position));
                //UserController.userD.get(position).setChatWith(al.get(position));
                startActivity(new Intent(UsersActivity.this, ChatActivity.class));
            }
        });
    }


}