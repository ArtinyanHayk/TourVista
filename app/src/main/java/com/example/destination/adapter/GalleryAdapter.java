package com.example.destination.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.GalleryImages;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {
    private List<GalleryImages> list;
    SendImage onSendImage;

    public GalleryAdapter(List<GalleryImages> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_items,parent,false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, final int position) {
        //
        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getPickUri())
                .into(holder.imageView);
        holder.imageView.setOnClickListener(v ->
                chooseImage(list.get(position).getPickUri()));


    }
    private void chooseImage(List<Uri> pickUri){
        onSendImage.onSend(pickUri);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GalleryHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public GalleryHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

    }
    public interface SendImage {
        void onSend(List<Uri> pickUri);
    }
    public void sendImage(SendImage sendImage){
        this.onSendImage = sendImage ;
    }





}
