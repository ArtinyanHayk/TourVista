package com.example.destination.adapter;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private static Context context;
    LatLng location;

    static OnPressed onPressed;
    private Handler handler;
    private Runnable runnable;
    private boolean isHolding = false;

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
            holder.hint.setVisibility(View.GONE);
       //     holder.rightChatLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.chat_transition_animation));
            holder.rightChatLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isHolding = true;
                            handler = new Handler();
                            handler.postDelayed(runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (isHolding) {
                                        // Вибрация
                                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                        if (vibrator != null) {
                                            vibrator.vibrate(100); // Вибрирует 500 мс
                                        }

                                        // Показываем диалог
                                        onPressed.delete(model.getId());
                                    }
                                }
                            }, 100); // Задержка перед выполнением задачи 2 секунды
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            isHolding = false;
                            handler.removeCallbacks(runnable); // Остановить выполнение задачи
                            break;
                    }
                    return true; // Обработали событие
                }
            });
            if(model.getImageUris()!= null){

                if(!model.getImageUris().isEmpty()) {

                    ArrayList<SlideModel> list = new ArrayList<>();
                    for (String url : model.getImageUris()) {
                        list.add(new SlideModel(url,  ScaleTypes.CENTER_CROP));
                    }
                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                    int screenWidth = displayMetrics.widthPixels;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.map_icon, options);
                    float aspectRatio = options.outWidth / (float) options.outHeight;
                    int calculatedHeight = (int) (screenWidth / aspectRatio);
                    ViewGroup.LayoutParams layoutParams = holder.sliderRight.getLayoutParams();
                    layoutParams.height = calculatedHeight;
                    holder.sliderRight.setLayoutParams(layoutParams);

                    holder.sliderRight.setVisibility(View.VISIBLE);
                    holder.sliderRight.setImageList(list);

                    holder.hint.setVisibility(View.VISIBLE);
                    holder.rightChatTextview.setText(model.getMessage());
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
        //    holder.leftChatLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.chat_transition_animation));
            holder.sliderLeft.setVisibility(View.GONE);


            if(model.getImageUris()!= null){
                if(!model.getImageUris().isEmpty()) {
                  //  Toast.makeText(context, "photo", Toast.LENGTH_SHORT).show();
                    ArrayList<SlideModel> list = new ArrayList<>();
                    for (String url : model.getImageUris()) {
                        list.add(new SlideModel(url, ScaleTypes.CENTER_CROP));
                    }
                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                    int screenWidth = displayMetrics.widthPixels;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.map_icon, options);
                    float aspectRatio = options.outWidth / (float) options.outHeight;
                    int calculatedHeight = (int) (screenWidth / aspectRatio);
                    ViewGroup.LayoutParams layoutParams = holder.sliderLeft.getLayoutParams();
                    layoutParams.height = calculatedHeight;
                    holder.sliderLeft.setLayoutParams(layoutParams);

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
        void delete(String id);
    }
    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }


    public static class MessagesViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout,rightChatLayout;
        TextView leftChatTextview,rightChatTextview,posterNameRight,posterNameLeft,hint;
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
            hint = itemView.findViewById(R.id.hint);


        }
        public void clickListener(GeoPoint location) {
            getLocationLeft.setOnClickListener(v -> onPressed.onGetLocation(location));
            getLocationRight.setOnClickListener(v -> onPressed.onGetLocation(location));
        }
    }
    private void showDeleteDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Delete Message")
                .setMessage("Do you want to delete this message?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Логика удаления сообщения
                    // Например: deleteMessage();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}