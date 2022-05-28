package com.example.petadoption;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.example.petadoption.databinding.ActivityNewPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NewPostActivity extends DrawerBaseActivity
{

    private EditText NewPostDescription;
    private EditText NewPostBreed;
    private EditText NewPostPetName;
    private EditText NewPostLocation;

    private Button CreateNewPost;
    private Uri postImageUri;
    private ImageView newPostImage;
    ActivityResultLauncher<String> mGetContent;

    // Nav Drawer stuff
    ActivityNewPostBinding activityNewPostBinding;


    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Button leftBtn, middleLeftBtn, middleRightBtn, rightBtn,btnChangePfp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityNewPostBinding = activityNewPostBinding.inflate(getLayoutInflater());
        setContentView(activityNewPostBinding.getRoot());
        allocateActivityTitle("New Post");

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        NewPostDescription = findViewById(R.id.New_Post_Description);
        NewPostBreed = findViewById(R.id.New_Post_Breed);
        NewPostPetName = findViewById(R.id.New_Post_PetName);
        CreateNewPost = findViewById(R.id.Create_New_Post);
        newPostImage = findViewById(R.id.New_Post_Image);
        NewPostLocation = findViewById(R.id.New_Post_Location);

        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });
        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent= new Intent(NewPostActivity.this,CropperActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent,101);
            }
        });

        CreateNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Breed = NewPostBreed.getText().toString();
                String petname = NewPostPetName.getText().toString();
                String description = NewPostDescription.getText().toString();
                String location = NewPostLocation.getText().toString();
                if (!TextUtils.isEmpty(description) && newPostImage != null) {

                    String randomName = UUID.randomUUID().toString();

                    StorageReference filePath = storageReference.child("post_images").child(randomName + ".jpg");
                    filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                        {
                            Task<Uri> downloadUri =  task.getResult().getStorage().getDownloadUrl();
                            if (task.isSuccessful())
                            {
                                DocumentReference docRef = firebaseFirestore.collection("Posts").document(current_user_id);

                                Map<String, Object> postMap = new HashMap<>();
                                postMap.put("image_url", downloadUri.toString());
                                postMap.put("desc", description);
                                postMap.put("user_id", current_user_id);
                                postMap.put("timestamp", FieldValue.serverTimestamp());
                                postMap.put("breed", Breed);
                                postMap.put("petname", petname);
                                postMap.put("location", location);
                                docRef.set(postMap).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {

                                            Toast.makeText(NewPostActivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                            Intent mainIntent = new Intent(NewPostActivity.this, PostViewer.class);
                                            startActivity(mainIntent);
                                            finish();
                                        }
                                        else
                                        {
                                            //error handle Oncomplete
                                        }
                                    }
                                });
                            } else {
                                //error handle task unsuccessful
                            }
                        }
                    });
                }
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

/*
        btnChangePfp = findViewById(R.id.btnChangePfp);
        btnChangePfp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(NewPostActivity.this, UsersActivity.class)); // edit when get chat system
            }
        });
*/

    }
      @Override
public void onActivityResult(int requestCode, int resultCode, Intent data)
      {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode ==-1 && requestCode == 101)
            {
                String result=data.getStringExtra("RESULT");
                if(result!=null)
                {
                    postImageUri = Uri.parse(result);
                }
                newPostImage.setImageURI(postImageUri);
            }
            else if (resultCode == UCrop.RESULT_ERROR)
            {
                final Throwable cropError = UCrop.getError(data);
            }
      }
}