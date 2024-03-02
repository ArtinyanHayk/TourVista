package com.example.destination.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.HomeModel;

import java.util.List;
import java.util.Random;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private List<HomeModel> list;
    Context context;

    public HomeAdapter(List<HomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item,parent,false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        holder.userNameTv.setText(list.get(position).getUsername());
        holder.timeTv.setText(list.get(position).getTimestapmp());

        int count = list.get(position).getLikeCount();
        if(count == 0) {
            holder.likeCountTv.setVisibility(View.INVISIBLE);
        } else if (count == 1) {
            holder.likeCountTv.setText(count + "like");
            
        } else{
            holder.likeCountTv.setText(count + "like");
        }

        Random random = new Random();
        int color = Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);
        Glide.with(context.getApplicationContext())
                .load(list.get(position).getPostImage())
                .placeholder(new ColorDrawable(color))
                .timeout(7000)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HomeHolder extends RecyclerView.ViewHolder{
        private ImageView profileImage;
        private TextView userNameTv,publisherTv, timeTv,likeCountTv, descriptionTv;
        private ImageView imageView;
        private ImageButton likeBtn, commentBtn, shareBtn, addLocationBtn, favoriteBtn;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image_view);
            //?
            imageView = itemView.findViewById(R.id.post_image);
            userNameTv = itemView.findViewById(R.id.username);
            publisherTv = itemView.findViewById(R.id.publisher);
            timeTv = itemView.findViewById(R.id.time);
            likeCountTv = itemView.findViewById(R.id.likes);
            likeBtn = itemView.findViewById(R.id.like);
            commentBtn = itemView.findViewById(R.id.comment);
            descriptionTv = itemView.findViewById(R.id.statusTV);
            shareBtn = itemView.findViewById(R.id.share);
            addLocationBtn = itemView.findViewById(R.id.get_location);
            favoriteBtn = itemView.findViewById(R.id.favorite);




        }
    }
}
