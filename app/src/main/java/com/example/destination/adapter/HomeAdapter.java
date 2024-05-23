package com.example.destination.adapter;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.example.destination.R;
import com.example.destination.model.HomeModel;
import com.example.destination.utils.FirbaseUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneMultiFactorAssertion;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private static List<HomeModel> list;
    private static Context context;
    static OnPressed onPressed;
    LatLng location;





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
    public void onBindViewHolder(@NonNull HomeHolder holder,  int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (list.get(position).getLocation() == null) {
            holder.getLocationBtn.setVisibility(View.GONE);
            location = null;
        } else {
            holder.getLocationBtn.setVisibility(View.VISIBLE);
             location = list.get(position).getLocation();// Добавлено, чтобы кнопка была видимой, если getLocation() не равен null
        }


        holder.userNameTv.setText(list.get(position).getUsername());
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat hm = new SimpleDateFormat("HH:mm", Locale.getDefault());


        String time = ymd.format(list.get(position).getPostedTime().toDate());

        String todayTime = ymd.format(com.google.firebase.Timestamp.now().toDate());
        //TimeISSUE

        if (todayTime.equals(time)){
            holder.timeTv.setText(hm.format(list.get(position).getPostedTime().toDate()));


        } else {
            holder.timeTv.setText(time);
        }





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
                holder.likeCheckBox.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_heart_red));
            } else {
                holder.likeCheckBox.setBackground(ContextCompat.getDrawable(context, R.drawable.like));
            }
        } else {
            holder.likeCountTv.setText("0 likes");
            holder.likeCheckBox.setBackground(ContextCompat.getDrawable(context,R.drawable.like));
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
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .placeholder(new ColorDrawable(color))
                .timeout(7000)

                .into(holder.imageView);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.drawable.map_icon, options);
        float aspectRatio = options.outWidth / (float) options.outHeight;
        int calculatedHeight = (int) (screenWidth / aspectRatio);

// Get the correct LayoutParams instance
        ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams) layoutParams;
            linearLayoutParams.height = calculatedHeight;
            holder.cardView.setLayoutParams(linearLayoutParams);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams relativeLayoutParams = (RelativeLayout.LayoutParams) layoutParams;
            relativeLayoutParams.height = calculatedHeight;
            holder.cardView.setLayoutParams(relativeLayoutParams);
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams frameLayoutParams = (FrameLayout.LayoutParams) layoutParams;
            frameLayoutParams.height = calculatedHeight;
            holder.cardView.setLayoutParams(frameLayoutParams);
        }
        holder.clickListener(position,
                list.get(position).getId(),
                list.get(position).getUsername(),
                list.get(position).getUid(),
                list.get(position).getLikes(),
                location,
                list.get(position).getImageUrl(),
                list.get(position).getProfileImage(),
                list.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnPressed {
        void onLiked(int position, String id, String uid, List<String> likeList);
        void onComment(int position, String id, String uid);
        void onGetLocation(int position, String id, String uid, LatLng location);
        void onSharePost(int position,String id,String uid,String postImageUrl,String profileImage,String username,String Description,LatLng location);
    }

    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }

    static class HomeHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView userNameTv, timeTv, likeCountTv, descriptionTv;
        private ImageView imageView;
        private ImageView likeCheckBox;
        private  ImageButton commentBtn, shareBtn, getLocationBtn, favoriteBtn;
        private CardView cardView;
       // LottieAnimationView likeAnim;

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

           // likeAnim = itemView.findViewById(R.id.likeAnim);
           //likeAnim.setMinAndMaxProgress(0.7f,1.0f);
            cardView = itemView.findViewById(R.id.cardView);








        }

        public void clickListener(final int position, final String id, final String username, final String uid, List<String> likes, LatLng location,String postImageUrl,String profileImage,String description) {
            likeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPressed.onLiked(position, id, uid, likes);
                   // if (likes.contains(FirbaseUtil.currentUsersId())){
                   //     likeAnim.setVisibility(View.VISIBLE);
//
                   //     likeAnim.playAnimation();
                   //     likeAnim.addAnimatorListener(new Animator.AnimatorListener() {
                   //         @Override
                   //         public void onAnimationStart(@NonNull Animator animation) {
//
                   //         }
//
                   //         @Override
                   //         public void onAnimationEnd(@NonNull Animator animation) {
                   //             likeAnim.setVisibility(View.INVISIBLE);
                   //         }
//
                   //         @Override
                   //         public void onAnimationCancel(@NonNull Animator animation) {
//
                   //         }
//
                   //         @Override
                   //         public void onAnimationRepeat(@NonNull Animator animation) {
//
                   //         }
                   //     });
                   // }

                }
            });



            getLocationBtn.setOnClickListener(v -> onPressed.onGetLocation(position, id, uid, location));

            commentBtn.setOnClickListener(v -> onPressed.onComment(position, id, uid));

            shareBtn.setOnClickListener(v -> onPressed.onSharePost(position,id,uid,postImageUrl,profileImage,username,description,location));
        }
    }
}