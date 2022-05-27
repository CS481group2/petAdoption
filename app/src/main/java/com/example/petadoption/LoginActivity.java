package com.example.petadoption;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100; // login activity shows the page for logging in and verifies login auth via firebase auth. goes to either main page if login is success or signup page

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText email, password;
    private Button buttonLogin;
    private TextView textRegister, fpassword, gLogin;
    private RelativeLayout layout;

    //for google signIN

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();  // initializing
        mUser = mAuth.getCurrentUser();
        email = findViewById(R.id.LoginEmail);
        password = findViewById(R.id.LoginPassword);
        buttonLogin = findViewById(R.id.Login);
        textRegister = findViewById(R.id.text_register);
        fpassword = findViewById(R.id.forgot_password);
        gLogin = findViewById(R.id.googleLogin);

        // keyboard close
        layout= findViewById(R.id.relative);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);

            }
        });


        //google sign in ( firebase auth:21.0.3)
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

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
        fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( LoginActivity.this, ForgotPassword.class));
            }
        });
    }
// google sign in
    private void SignIn(){
    Intent signInIntent= mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
         Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
         try{
             // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            FirebaseAuthWithGoogle(account.getIdToken());
         } catch (ApiException e) {
             //Google Sign Failed,

             e.printStackTrace();
             Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
             finish();
         }

        }

        }


    private void FirebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Toast.makeText(LoginActivity.this,"DONE",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();


                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            //dialog.dismiss();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser user){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                        UserDetails.name = user;
                        UserDetails.password = pass;
                        UserDetails.uid = mUser.getUid();
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