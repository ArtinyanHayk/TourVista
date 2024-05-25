package com.example.destination.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.CommentModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Random;


public class CommentAdapter extends  RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private List<CommentModel> list;
    Context context;
    private static OnPressed onPressed;

    public CommentAdapter(List<CommentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.userNameTv.setText(list.get(position).getUserName());
        holder.commentTv.setText(list.get(position).getCommentText());
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);

        if(list.get(position).getUId().equals(user.getUid())){
            holder.delete.setVisibility(View.VISIBLE);
        }else{
            holder.delete.setVisibility(View.GONE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressed.onDelete(list.get(position).getCommentId(),list.size());
            }
        });











    }
    public interface OnPressed {
        void onDelete(String commentId,int position);
      }

    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder{
        private ImageView profileImage;
        private TextView userNameTv,  timeTv, likeCountTv, replyTv,commentTv;
        private CheckBox likeCheckBox;
        private ImageButton delete;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            userNameTv = itemView.findViewById(R.id.userName);
            commentTv = itemView.findViewById(R.id.comment_text);
            delete = itemView.findViewById(R.id.delete);
        }



    }



}
