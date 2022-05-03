package com.example.petadoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText resetemail;
    private TextView backbtn;
    private Button resetbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        resetemail = findViewById(R.id.resetEmail);
        resetbtn = findViewById(R.id.resetButton);
        mAuth = FirebaseAuth.getInstance();   // for authentication firebase
        backbtn = findViewById(R.id.backtologin);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(ForgotPassword.this, LoginActivity.class));
            }
        });

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpassword();
            }
        });
    }
    private void resetpassword(){
        String email= resetemail.getText().toString().trim();

        if(email.isEmpty()){
            resetemail.setError("Email is required");
            resetemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            resetemail.setError("Please Provide valid email!");
            resetemail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ForgotPassword.this, "Something went wrong! Try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}