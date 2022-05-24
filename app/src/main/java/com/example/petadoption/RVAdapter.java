package com.example.petadoption;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private List<Animal>list_data;
    private Context ct;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Button rowDescription;
        ImageView rowImage;

        public ViewHolder(View itemView){
            super(itemView);
            rowDescription = (Button)itemView.findViewById(R.id.message_button);
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
        holder.rowDescription.setText(ld.getDesc());
        //Picasso.get().load(ld.getImage_url()).into(holder.rowImage);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }
}