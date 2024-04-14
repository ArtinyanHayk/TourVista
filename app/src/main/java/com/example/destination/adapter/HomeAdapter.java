package com.example.destination.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.HomeModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneMultiFactorAssertion;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;
import java.util.Random;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private static List<HomeModel> list;
    private static Context context;
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

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder,  int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(list.get(position).getLocation() == null){
            Toast.makeText(context, "gone", Toast.LENGTH_SHORT).show();
            holder.getLocationBtn.setVisibility(View.GONE);
        }


        holder.userNameTv.setText(list.get(position).getUsername());
        holder.timeTv.setText("" + list.get(position).getTimestapmp());

        if (list.get(position).getLikes() != null) {
            List<String> likeList = list.get(position).getLikes();
            int count = likeList.size();
            if (count == 0) {
                holder.likeCountTv.setText(count + " like");
            } else if (count == 1) {
                holder.likeCountTv.setText(count + " like");
            } else {
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

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);
        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImageUrl())
                .placeholder(new ColorDrawable(color))
                .timeout(7000)
                .into(holder.imageView);

        // Dynamic height calculation for ImageView
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.drawable.map_icon, options);
        float aspectRatio = options.outWidth / (float) options.outHeight;
        int calculatedHeight = (int) (screenWidth / aspectRatio);
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.height = calculatedHeight;
        holder.imageView.setLayoutParams(layoutParams);

        holder.clickListener(position,
                list.get(position).getId(),
                list.get(position).getUsername(),
                list.get(position).getUid(),
                list.get(position).getLikes(),
                list.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnPressed {
        void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked);
        void onComment(int position, String id, String uid);
        void onGetLocation(int position, String id, String uid, LatLng location);
    }

    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }

    static class HomeHolder extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView userNameTv, timeTv, likeCountTv, descriptionTv;
        private ImageView imageView;
        private CheckBox likeCheckBox;
        private  ImageButton commentBtn, shareBtn, getLocationBtn, favoriteBtn;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_profile);
            imageView = itemView.findViewById(R.id.post_image);
            userNameTv = itemView.findViewById(R.id.username);
            timeTv = itemView.findViewById(R.id.time);
            likeCountTv = itemView.findViewById(R.id.likeCountTv);
            descriptionTv = itemView.findViewById(R.id.descTv);
            likeCheckBox = itemView.findViewById(R.id.like);
            commentBtn = itemView.findViewById(R.id.comment);
            shareBtn = itemView.findViewById(R.id.share);
            getLocationBtn = itemView.findViewById(R.id.get_location);
            favoriteBtn = itemView.findViewById(R.id.favorite);







        }

        public void clickListener(final int position, final String id, final String username, final String uid, List<String> likes, LatLng location) {
            likeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onPressed.onLiked(position, id, uid, likes, isChecked);
                }
            });

            getLocationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onPressed.onGetLocation(position, id, uid, location);
                }
            });

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPressed.onComment(position, id, uid);
                }
            });
        }
    }
}