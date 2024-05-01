package com.example.destination.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.UserModel;
import com.example.destination.utils.FirbaseUtil;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendLayoutAdapter extends RecyclerView.Adapter<SendLayoutAdapter.SendLayoutViewHolder> {
    Context context;
    List<UserModel> list;
    onPressed pressed;

    public SendLayoutAdapter(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SendLayoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.share_user_item,parent,false);
        return new SendLayoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SendLayoutViewHolder holder, int position) {
        UserModel model =  list.get(position);

        holder.username.setText(model.getUserName());

        if(model.getonline()){
            holder.online.setVisibility(View.VISIBLE);
        }else{
            holder.online.setVisibility(View.GONE);
        }

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(model.getImageURL())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profilePic);

        holder.setOnClickListener(model.getUserId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface onPressed{
        void onSelect(String id);
    }
    public void onPressed(onPressed pressed){this.pressed = pressed;}


    class SendLayoutViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profilePic;
        TextView username;
        View online;

        public SendLayoutViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            username = itemView.findViewById(R.id.username);
            online = itemView.findViewById(R.id.online);

        }
        public void setOnClickListener(String id){
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressed.onSelect(id);
                }
            });
        }
    }
}
