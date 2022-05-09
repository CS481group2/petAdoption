package com.example.petadoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password, fName, lName;
    private Button buttonSignUp;
    private TextView textExisting;
    private FirebaseFirestore fdb;
    private String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();  // initializing
        fName = findViewById(R.id.SignUpFname);
        lName = findViewById(R.id.SignUpLname);
        email = findViewById(R.id.SignUpEmail);
        password = findViewById(R.id.SignUpPassword);
        buttonSignUp = findViewById(R.id.SignUp);
        textExisting = findViewById(R.id.text_existing);
        fdb = FirebaseFirestore.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                SignUp();
            }
        });

        textExisting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class)); // switch to login screen if returning user
            }
        });

    }



    private void SignUp()
    {
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String firstName = fName.getText().toString().trim();
        String lastName = lName.getText().toString().trim();


        //pass word special char test
        Pattern p= Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher match=p.matcher(pass);
        boolean check = match.find();
        if (user.isEmpty())
        {
            email.setError("Email can not be empty");
        }

        if (pass.isEmpty())
        {
            password.setError("Password can not be empty");
        }

        if (firstName.isEmpty())
        {
            fName.setError("First name can not be empty");
        }

        if (lastName.isEmpty())
        {
            lName.setError("Last name can not be empty");
        }
        if(check == false){
            password.setError("Special Character needed");
        }
        if ( pass.length() < 6 ){
            password.setError("Password Must be 6 Character long ");
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        UID = mAuth.getCurrentUser().getUid();
                        DocumentReference docRef = fdb.collection("users").document(UID);

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("firstName", firstName);                   // can add more things to the document from registration form! example here
                        userData.put("lastName", lastName);
                        userData.put("email", user);
                        docRef.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() // for error handeling but can prob just say docRef.set(userData);
                        {
                            @Override
                            public void onSuccess(Void unused)
                            {
                                Log.d("hm", "on success : stuff... " + UID );

                            }
                        });

                        Toast.makeText(SignUpActivity.this, "User Registration Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));  // go to login page if successful

                    }

                    else
                    {
                        Toast.makeText(SignUpActivity.this, "Registration Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

}