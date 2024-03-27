package com.example.destination.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.HomeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Random;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private List<HomeModel> list;
    Context context;
    static OnPressed onPressed;

    public HomeAdapter(List<HomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new HomeHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        holder.userNameTv.setText(list.get(position).getUsername());
        holder.timeTv.setText("" + list.get(position).getTimestapmp());



        if (list.get(position).getLikes() != null) {
            Log.e("!null","likes");
            List<String> likeList = list.get(position).getLikes();
            int count = likeList.size();
            if (count == 0) {
                Log.e("0like","work");
                holder.likeCountTv.setText(count + " like");
            } else if (count == 1) {
                Log.e("1like","work");
                holder.likeCountTv.setText(count + " like");
            } else {
                Log.e("likes","work");
                holder.likeCountTv.setText(count + " likes");
            }

            if(likeList.contains(user.getUid())) {
                holder.likeCheckBox.setChecked(true);
            } else {
                holder.likeCheckBox.setChecked(false);
            }
        } else {
            holder.likeCountTv.setText("0 likes");
            holder.likeCheckBox.setChecked(false);
        }

        holder.descriptionTv.setText(list.get(position).getDescription());

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
if(list.get(position).getProfileImage() != null){
    //Toast.makeText(context, list.get(position).getProfileImage(), Toast.LENGTH_SHORT).show();
}
        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(7000)
                .into(holder.profileImage);
        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImageUrl())
                .placeholder(new ColorDrawable(color))
                .timeout(7000)
                .into(holder.imageView);

        holder.clickListener(position,
                list.get(position).getId(),
                list.get(position).getUsername(),
                list.get(position).getUid(),
                list.get(position).getLikes());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    ////////////////
    public  interface OnPressed{
        void onLiked(int position,String id,String  uid, List<String> likeList,boolean isChecked);
        void onComment(int position,String id,String comment);

    }
    ///////////////
    public void OnPressed(OnPressed onPressed){
        this.onPressed = onPressed;
    }

    static class HomeHolder extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView userNameTv,  timeTv, likeCountTv, descriptionTv;
        private ImageView imageView;
        private CheckBox likeCheckBox;
        private ImageButton  commentBtn, shareBtn, getLocationBtn, favoriteBtn;




        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_profile);
            //?
            imageView = itemView.findViewById(R.id.post_image);
            userNameTv = itemView.findViewById(R.id.username);
            timeTv = itemView.findViewById(R.id.time);
            likeCountTv = itemView.findViewById(R.id.likeCountTv);
            descriptionTv = itemView.findViewById(R.id.descTv);
            likeCheckBox  = itemView.findViewById(R.id.like);
            commentBtn = itemView.findViewById(R.id.comment);
            shareBtn = itemView.findViewById(R.id.share);
            getLocationBtn = itemView.findViewById(R.id.get_location);
            favoriteBtn = itemView.findViewById(R.id.favorite);


        }

        public void clickListener(final int position, final String id, final String username, final String uid, List<String> likes) {
            likeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onPressed.onLiked(position,id,uid,likes,isChecked);
                }
            });
        }
    }


}
