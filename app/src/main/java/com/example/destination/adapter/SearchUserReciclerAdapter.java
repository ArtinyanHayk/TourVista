package com.example.destination.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.HomeModel;
import com.example.destination.model.PostImageModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.FirbaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserReciclerAdapter extends RecyclerView.Adapter<SearchUserReciclerAdapter.UserModelViewHolder> {
    Context context;
    List<UserModel> list;
     onSelected selected;
    onPressed pressed;

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



        if(list.get(position).getPhone() == null){
            holder.searchLayout.setVisibility(View.GONE);
            holder.username.setText(list.get(position).getUserName());
            Glide.with(context.getApplicationContext())
                    .load(list.get(position).getImageURL())
                    .placeholder(R.drawable.ic_person)
                    .timeout(6500)
                    .into(holder.shareProfilePic);
            if(list.get(position).getonline()){
                holder.online.setVisibility(View.VISIBLE);
            }else{
                holder.online.setVisibility(View.GONE);
            }

            holder.setOnClickListener(list.get(position).getUserId());
        }else{
            holder.shareLayout.setVisibility(View.GONE);
            holder.usernameText.setText(list.get(position).getUserName());
            if(!list.get(position).getUserId().equals(FirbaseUtil.currentUsersId())){
                if(list.get(position).getFollowers().contains(FirbaseUtil.currentUsersId())){
                    holder.follow.setText("followed");
                }else{
                    holder.follow.setText("");
                }
            }else{
                holder.follow.setVisibility(View.GONE);
            }

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
                    0);

        }





    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface onSelected{
        public void onSelect(int position, String Uid, String Username, String imageURL, List<String> followers, List<String> following, int posts );
    }
    public void onSelected(onSelected selected){this.selected = selected;}

    public interface onPressed{
        void onPress(String id);
    }
    public void onPressed(onPressed pressed){this.pressed = pressed;}
    class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText,username,follow;
       CircleImageView profilePic,shareProfilePic;

       RelativeLayout searchLayout,shareLayout;
       View online;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            searchLayout = itemView.findViewById(R.id.search_layout);
            usernameText = itemView.findViewById(R.id.user_name_text);
            profilePic = itemView.findViewById(R.id.search_image_profile);
            shareLayout = itemView.findViewById(R.id.share_layout);
            username = itemView.findViewById(R.id.username);
            shareProfilePic = itemView.findViewById(R.id.profilePic);
            online = itemView.findViewById(R.id.online);
           follow = itemView.findViewById(R.id.followIndicator);


        }

        public void setOnClickListener(int position, String Uid,String Username,String imageURL,List<String> followers,List<String> following,int posts) {
            searchLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected.onSelect(position,Uid,Username,imageURL,followers,following,posts);
                }
            });
        }
        public void setOnClickListener(String id){
            shareProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressed.onPress(id);
                }
            });
        }
    }
}
