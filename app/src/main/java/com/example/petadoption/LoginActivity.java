package com.example.petadoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity { // login activity shows the page for logging in and verifies login auth via firebase auth. goes to either main page if login is success or signup page

    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button buttonLogin;
    private TextView textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();  // initializing
        email = findViewById(R.id.LoginEmail);
        password = findViewById(R.id.LoginPassword);
        buttonLogin = findViewById(R.id.Login);
        textRegister = findViewById(R.id.text_register);

        buttonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Login();

            }
        });

        textRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));  // click on register go to register this is how u switch scenes
            }
        });
    }

    private void Login() // prob add things like password requirements in here
    {
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (user.isEmpty())
        {
            email.setError("Email can not be empty");
        }

        if (pass.isEmpty())
        {
            password.setError("Password can not be empty");
        }

        else
        {
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() // checks firebase db 
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class)); // if login successful move to mainActivity
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}