package com.example.petadoption;

// import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.Firebase;
//import com.google.firebase.FirebaseError;

import com.example.petadoption.databinding.ActivityChatBinding;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends DrawerBaseActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;

    private FirebaseUser user;
    private String userID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Nav Drawers Stuff
    ActivityChatBinding activityChatBinding;

    private boolean working = false;
    public boolean getUsername(String user, String uid)
    {
        /*for(int i = 0; i < userD.size(); i++)
        {
            if(user.equals(userD.get(i).getName())) return true;
        }*/

        if (user.equals(UsersActivity.currentUser.getName()))
        {
            return true;
        }
        return false;

        /*DocumentReference docRef = FirebaseFirestore.getInstance() .collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if (user.equals(task.getResult().getString("firstName") + " " + task.getResult().getString("lastName")))
                    {
                        Log.println(Log.ASSERT, "Passed Name", user);
                        Log.println(Log.ASSERT, "Checked Name", task.getResult().getString("firstName") + " " + task.getResult().getString("lastName"));
                        working = true;
                    }
                }
            }
        });
        if(working)
        {
            working = false;
            return true;
        }
        return false;*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());
        allocateActivityTitle(UsersActivity.currentUser.getChatWith());

        /*for (int i = 0; i < UserController.userD.size(); i++)
        {
            Log.println(Log.ASSERT, "USER", UserController.userD.get(i).getName());
        }*/

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://petadoption-e740c-default-rtdb.firebaseio.com/messages/" + UsersActivity.currentUser.getName() + "_" + UsersActivity.currentUser.getChatWith());
        Log.println(Log.ASSERT, "PLEASE", UsersActivity.currentUser.getName());
        Log.println(Log.ASSERT, "WHOMST", UsersActivity.currentUser.getChatWith());
        reference2 = new Firebase("https://petadoption-e740c-default-rtdb.firebaseio.com/messages/" + UsersActivity.currentUser.getChatWith() + "_" + UsersActivity.currentUser.getName());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UsersActivity.currentUser.getName());

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
                messageArea.setText("");
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                Log.println(Log.ASSERT, "SENT BY", userName);
                if((getUsername(userName, userID))){

                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(UsersActivity.currentUser.getChatWith() + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setTextColor(Color.BLACK);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}