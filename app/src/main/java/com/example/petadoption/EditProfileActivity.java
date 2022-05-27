package com.example.petadoption;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petadoption.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class EditProfileActivity extends DrawerBaseActivity
{
    private Button btnChangePfp;
    private ImageView pfp;
    // firebase tools for uploading images and retriving the name
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;
    TextView emailTextView, fnameTextView;

    // Dashboard stuff
    ActivityEditProfileBinding activityEditProfileBinding;

    // firestore stuffs
    private  static  final String KEY_FIRSTNAME= "firstName";
    private  static  final String KEY_LASTNAME= "lastName";
    private  static  final String KEY_EMAIL= "email";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityEditProfileBinding = activityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(activityEditProfileBinding.getRoot());
        allocateActivityTitle("Edit Profile");

        pfp = findViewById(R.id.pfp);
        btnChangePfp= findViewById(R.id.btnChangePfp);

        // firebase stuffs
        mAuth= FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        user= FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();




        emailTextView = findViewById(R.id.txtemail);
        fnameTextView = findViewById(R.id.txtfName);
        //lnameTextView = findViewById(R.id.txtlName); we don't need it

        // emailTextView.setText(userID);
        // getting data from database
        DocumentReference docRef = FirebaseFirestore.getInstance() .collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String firstname= task.getResult().getString(KEY_FIRSTNAME);
                    String lastname = task.getResult().getString(KEY_LASTNAME);
                    String email= task.getResult().getString(KEY_EMAIL);

                    fnameTextView.setText(firstname + " "+ lastname);
                    // lnameTextView.setText(lastname);
                    emailTextView.setText(email);
                }
            }
        });

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33 );

            }
        });

        btnChangePfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage();
            }

        });
        // showing old picture before updating
        final StorageReference imgRef = storage.getReference().child("profile_picture").child(FirebaseAuth.getInstance().getUid());
        try {
            File localfile= File.createTempFile("tempfile", ".jpg");
            imgRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    pfp.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



/*
        btnChangePfp = findViewById(R.id.btnChangePfp);
        btnChangePfp.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, MainActivity.class)); // edit when get chat system
            }
        });*/

        //pfp = findViewById(R.id.pfp);

    }
    private void updateImage() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null){
            Uri profileUri =data.getData();
            pfp.setImageURI(profileUri);
            // if new profile picture added delete old one
            final StorageReference ref = storage.getReference().child("profile_picture").child(FirebaseAuth.getInstance().getUid());
            ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //nothing just file is deleted
                }
            });
            // one the file is deleted.
            final StorageReference reference = storage.getReference().child("profile_picture").child(FirebaseAuth.getInstance().getUid());
            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    // new photo is uploaded now fetch it to the image view.
                    final StorageReference imgRef = storage.getReference().child("profile_picture").child(FirebaseAuth.getInstance().getUid());
                    try {
                        File localfile= File.createTempFile("tempfile", ".jpg");
                        imgRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                pfp.setImageBitmap(bitmap);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    // placeholder for toolbar button functionality and profile
/*    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.leftBtn:
                Toast.makeText(this, "Left Button Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.middleLeftBtn:
                Toast.makeText(this, "Middle Left Button Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.middleRightBtn:
                Toast.makeText(this, "Middle Right Button Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rightBtn:
                Toast.makeText(this, "Right Button Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnChangePfp:
                // Open Galley
                // Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                Toast.makeText(this, "Profile button Pressed", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }*/
}