package com.example.destination.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendLayoutAdapter extends RecyclerView.Adapter<SendLayoutAdapter.SendLayoutViewHolder> {
    Context context;
    List<UserModel> list;

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
        Toast.makeText(context, "works", Toast.LENGTH_SHORT).show();

        holder.username.setText(list.get(position).getUserName());

        if(list.get(position).getonline()){
            holder.online.setVisibility(View.VISIBLE);
        }else{
            holder.online.setVisibility(View.GONE);
        }

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImageURL())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


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
    }
}
