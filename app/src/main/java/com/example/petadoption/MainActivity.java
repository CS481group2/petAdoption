package com.example.petadoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonLogOut;

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
        startActivity(new Intent( MainActivity.this, LoginActivity.class));  // switch scenes when log in is clicked?
    }
}