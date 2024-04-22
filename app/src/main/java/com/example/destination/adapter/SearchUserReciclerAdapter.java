package com.example.destination.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.UserModel;

import java.util.List;

public class SearchUserReciclerAdapter extends RecyclerView.Adapter<SearchUserReciclerAdapter.UserModelViewHolder> {
    Context context;
    List<UserModel> list;
     onSelected selected;

    public SearchUserReciclerAdapter(List<UserModel> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserModelViewHolder holder, int position) {
        holder.usernameText.setText(list.get(position).getUserName());
        holder.phoneText.setText(list.get(position).getPhone());

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImageURL())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profilePic);

        holder.setOnClickListener(position,
                list.get(position).getUserId(),
                list.get(position).getUserName(),
                list.get(position).getImageURL(),
                list.get(position).getFollowers(),
                list.get(position).getFollowing(),
                0

        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface onSelected{
        public void onSelect(int position, String Uid, String Username, String imageURL, List<String> followers, List<String> following, int posts );
    }
    public void onSelected(onSelected selected){this.selected = selected;}


    class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView phoneText;
       ImageView profilePic;
       LinearLayout layout;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.user_item);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.search_image_profile);

        }

        public void setOnClickListener(int position, String Uid,String Username,String imageURL,List<String> followers,List<String> following,int posts) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected.onSelect(position,Uid,Username,imageURL,followers,following,posts);
                }
            });
        }
    }
}
