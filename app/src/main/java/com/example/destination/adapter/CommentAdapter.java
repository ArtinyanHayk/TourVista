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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Random;


public class CommentAdapter extends  RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private List<CommentModel> list;
    Context context;

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
        holder.timeTv.setText("" + list.get(position).getTimestapmp());


      //  if (list.get(position).getLikeList() != null) {
      //      Log.e("!null","likes");
      //      List<String> likeList = list.get(position).getLikeList();
      //      int count = likeList.size();
      //      if (count == 0) {
      //          Log.e("0like","work");
      //          holder.likeCountTv.setText(count + " like");
      //      } else if (count == 1) {
      //          Log.e("1like","work");
      //          holder.likeCountTv.setText(count + " like");
      //      } else {
      //          Log.e("likes","work");
      //          holder.likeCountTv.setText(count + " likes");
      //      }
//
      //      if(likeList.contains(user.getUid())) {
      //          holder.likeCheckBox.setChecked(true);
      //      } else {
      //          holder.likeCheckBox.setChecked(false);
      //      }
      //  } else {
      //      holder.likeCountTv.setText("0 likes");
      //      holder.likeCheckBox.setChecked(false);
      //  }

        holder.commentTv.setText(list.get(position).getCommentText());

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);
      //  holder.clickListener(position,
      //          list.get(position).getId(),
      //          list.get(position).getUsername(),
      //          list.get(position).getUid(),
      //          list.get(position).getLikes(),
      //          list.get(position).getComments(),
      //          list.get(position).getLocation());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class CommentHolder extends RecyclerView.ViewHolder{
        private ImageView profileImage;
        private TextView userNameTv,  timeTv, likeCountTv, replyTv,commentTv;
        private CheckBox likeCheckBox;
        private ImageButton threeDots;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            userNameTv = itemView.findViewById(R.id.userName);
            timeTv = itemView.findViewById(R.id.comment_time);
           // likeCountTv = itemView.findViewById(R.id.like_count);
           // replyTv = itemView.findViewById(R.id.reply_button);
           // likeCheckBox = itemView.findViewById(R.id.like_button);
           // threeDots = itemView.findViewById(R.id.three_dots_button);
            commentTv = itemView.findViewById(R.id.comment_text);
        }

    }
}
