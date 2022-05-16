package com.example.petadoption;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonLogOut;
    private Button leftBtn, middleLeftBtn, middleRightBtn, rightBtn,btnChangePfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();   // for authentication firebase
        buttonLogOut = findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                logout();
            }
        });

        leftBtn = findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
            }
        });

        middleLeftBtn = findViewById(R.id.middleLeftBtn);
        middleLeftBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
/*
        middleRightBtn = findViewById(R.id.middleRightBtn);
        middleRightBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, MainActivity.class)); // edit when get post viewer scene
            }
        });
*/
        rightBtn = findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, NewPostActivity.class)); // edit when get create post scene
            }
        });

        btnChangePfp = findViewById(R.id.btnChangePfp);
        btnChangePfp.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, UsersActivity.class)); // edit when get chat system
            }
        });

    }

    @Override
    public void onStart() // if no user is logged in then move to login activity if logged in then main activity
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        }


    }
    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent( MainActivity.this, LoginActivity.class));  // switch scenes when logged out
    }
}