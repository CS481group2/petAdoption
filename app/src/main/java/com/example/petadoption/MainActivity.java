package com.example.petadoption;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.petadoption.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends DrawerBaseActivity {

    private FirebaseAuth mAuth;
    private Button buttonLogOut;
    private Button leftBtn, middleLeftBtn, middleRightBtn, rightBtn,btnChangePfp;

    // Nav drawer stuff
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = activityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle("Sign Off");

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