package com.example.petadoption;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
       holder.petdesc.setText(ld.getDesc()+(ld.getImage_url()));
        //Picasso.get().load(ld.getImage_url()).into(holder.rowImage);
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

    @Override
    public int getItemCount() {
        return list_data.size();
    }
}