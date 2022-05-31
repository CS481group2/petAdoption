package com.example.petadoption;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private List<Animal>list_data;
    private Context ct;
    FirebaseStorage storage;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView petname, petbreed, petdesc, location,time;
        ImageView image;
        Button msg;

        public ViewHolder(View itemView){
            super(itemView);
            petname= (TextView) itemView.findViewById(R.id.petName);
            image= (ImageView) itemView.findViewById(R.id.postImage);
           // petname = (TextView) itemView.findViewById(R.id.petName);
           petbreed = (TextView) itemView.findViewById(R.id.petBreed);
           location = (TextView) itemView.findViewById(R.id.petLocation);
           //time = (TextView) itemView.findViewById(R.id.postTime);
          petdesc = (TextView) itemView.findViewById(R.id.petDec);;
            //rowImage = (ImageView)itemView.findViewById(R.id.imageView);
            msg = (Button) itemView.findViewById(R.id.btnMessage);
        }
    }

    public RVAdapter(List<Animal> list_data, Context ct) {
        this.list_data = list_data;
        this.ct = ct;
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rows,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Animal ld=list_data.get(position);
        //holder.rowDescription.setText(ld.getDesc());
        holder.petname.setText(ld.getPetname());
        holder.petbreed.setText(ld.getBreed());
        holder.location.setText(ld.getLocation());

        //holder.time.setText(ld.getTimestamp());
       holder.petdesc.setText(ld.getDesc());
        //Picasso.get().load(ld.getImage_url()).into(holder.rowImage);

        Glide.with(holder.image.getContext()).load(ld.getImage_url()).into(holder.image);

        holder.msg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Log.println(Log.ASSERT, "user???", ld.getUser_id() + " ");
                final String[] username = {""};
                db.getInstance().collection("users")
                        .document(ld.getUser_id()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                DocumentSnapshot document = task1.getResult();
                                username[0] = document.getString("firstName") + " " + document.getString("lastName");
                            }
                        });
                DocumentReference postRef = db.collection("users").document(ld.getUser_id());
                postRef.update("chattingWith", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()));

                DocumentReference userRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Log.println(Log.ASSERT, "user???", ld.getUser_id() + " ");
                userRef.update("chattingWith", FieldValue.arrayUnion(ld.getUser_id()));
                DocumentReference docRef = FirebaseFirestore.getInstance() .collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            UsersActivity ud = new UsersActivity();
                            runUserChat(task, ld.getUser_id(), username[0]);
                        }
                    }
                });
                //final Map<String,Object> addId = new HashMap<>();
                //addId.put("chattingWith", FieldValue.arrayUnion(ld.getUId()));

                //db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(addId);
            }
        });

        /*
        storage= FirebaseStorage.getInstance();
        final StorageReference imgRef = storage.getReference().child("post_images").child(ld.getImage_url());

            imgRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                    //holder.image.setImageBitmap(bitmap);
                    holder.image.setImageBitmap(bitmap);

                }
            });
            /*
            File localfile= File.createTempFile("tempfile", ".jpg");
            imgRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    holder.image.setImageBitmap(bitmap);
                }
            });

             */

    }

    public void runUserChat(Task<DocumentSnapshot> task, String uid, String name)
    {
        UsersActivity.currentUser = new UserDetails(task.getResult().getString("firstName") + " " + task.getResult().getString("lastName"), task.getResult().getId());
        Log.println(Log.ASSERT, "???", UsersActivity.currentUser.getName());
        UsersActivity.currentUser.setChatWith(name);
        Log.println(Log.ASSERT, "IS this working?", UsersActivity.currentUser.getName());
        //UserController.userD.get(position).setChatWith(al.get(position));
        ct.startActivity(new Intent(ct, ChatActivity.class));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }
}