package com.example.destination.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.destination.R;
import com.example.destination.model.MessageModel;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends FirestoreRecyclerAdapter<MessageModel, MessagesAdapter.MessagesViewHolder> {
    private Context context;
    LatLng location;

    static OnPressed onPressed;

    public MessagesAdapter(FirestoreRecyclerOptions<MessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_item, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessagesViewHolder holder, int position, @NonNull MessageModel model) {
        if(model.getSenderId().equals(FirbaseUtil.currentUsersId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextview.setText(model.getMessage());
            holder.sliderRight.setVisibility(View.GONE);
            holder.rightChatLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.chat_transition_animation));
            Toast.makeText(context, "photo1", Toast.LENGTH_SHORT).show();
            if(model.getImageUris()!= null){
                Toast.makeText(context, "photo2", Toast.LENGTH_SHORT).show();
                if(!model.getImageUris().isEmpty()) {
                    Toast.makeText(context, "photo3", Toast.LENGTH_SHORT).show();
                    ArrayList<SlideModel> list = new ArrayList<>();
                    for (String url : model.getImageUris()) {
                        list.add(new SlideModel(url,  ScaleTypes.CENTER_CROP));
                    }
                    holder.sliderRight.setVisibility(View.VISIBLE);
                    holder.sliderRight.setImageList(list);
                }
                if (model.getLocation() == null) {
                    holder.getLocationRight.setVisibility(View.GONE);

                } else {
                    holder.getLocationRight.setVisibility(View.VISIBLE);
                }
                if(model.getProfileImage()!= null){
                    holder.postLayoutRight.setVisibility(View.VISIBLE);
                    holder.posterNameRight.setText(model.getUsername());
                    if(!model.getProfileImage().isEmpty()){
                        Glide.with(context.getApplicationContext())
                                .load(model.getProfileImage())
                                .placeholder(R.drawable.ic_person)
                                .timeout(6500)
                                .into(holder.profilePicRight);
                    }
                }else{
                    holder.postLayoutRight.setVisibility(View.GONE);
                }
            }
        }else{
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextview.setText(model.getMessage());
            holder.leftChatLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.chat_transition_animation));
            holder.sliderLeft.setVisibility(View.GONE);
            if(model.getImageUris()!= null){
                if(!model.getImageUris().isEmpty()) {
                    Toast.makeText(context, "photo", Toast.LENGTH_SHORT).show();
                    ArrayList<SlideModel> list = new ArrayList<>();
                    for (String url : model.getImageUris()) {
                        list.add(new SlideModel(url, ScaleTypes.CENTER_CROP));
                    }
                    holder.sliderLeft.setVisibility(View.VISIBLE);
                    holder.sliderLeft.setImageList(list);
                }
            }

            if (model.getLocation() == null) {
                holder.getLocationLeft.setVisibility(View.GONE);
            } else {
                holder.getLocationLeft.setVisibility(View.VISIBLE);
            }
            if(model.getProfileImage()!= null){
                holder.postLayoutLeft.setVisibility(View.VISIBLE);
                holder.posterNameLeft.setText(model.getUsername());
                if(!model.getProfileImage().isEmpty()){
                    Glide.with(context.getApplicationContext())
                            .load(model.getProfileImage())
                            .placeholder(R.drawable.ic_person)
                            .timeout(6500)
                            .into(holder.profilePicLeft);
                }
            }else{
                holder.postLayoutLeft.setVisibility(View.GONE);
            }
        }
        holder.clickListener(model.getLocation());
    }
    @Override
    public int getItemCount() {
        return super.getItemCount(); // Ensure the item count is correct
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    public interface OnPressed {
        void onGetLocation(GeoPoint location);
    }
    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }


    public static class MessagesViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout,rightChatLayout;
        TextView leftChatTextview,rightChatTextview,posterNameRight,posterNameLeft;
        ImageSlider sliderLeft,sliderRight;
        ImageView profilePicLeft,profilePicRight;
        ImageButton getLocationLeft,getLocationRight;
        LinearLayout postLayoutLeft,postLayoutRight;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
            sliderLeft = itemView.findViewById(R.id.image_slider);
            sliderRight = itemView.findViewById(R.id.image_slider2);
            getLocationLeft = itemView.findViewById(R.id.get_location);
            getLocationRight = itemView.findViewById(R.id.get_location2);
            postLayoutLeft = itemView.findViewById(R.id.postLayoutLeft);
            postLayoutRight = itemView.findViewById(R.id.postLayoutRight);
            profilePicLeft = itemView.findViewById(R.id.image_profile);
            profilePicRight = itemView.findViewById(R.id.image_profile2);
            posterNameLeft = itemView.findViewById(R.id.poster_name);
            posterNameRight = itemView.findViewById(R.id.poster_name2);


        }
        public void clickListener(GeoPoint location) {
            getLocationLeft.setOnClickListener(v -> onPressed.onGetLocation(location));
            getLocationRight.setOnClickListener(v -> onPressed.onGetLocation(location));
        }
    }
}