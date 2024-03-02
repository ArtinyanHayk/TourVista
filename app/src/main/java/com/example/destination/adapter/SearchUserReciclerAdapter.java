package com.example.destination.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.destination.R;
import com.example.destination.model.UserModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserReciclerAdapter extends FirestoreRecyclerAdapter<UserModel,SearchUserReciclerAdapter.UserModelViewHolder> {
    Context context;

    public SearchUserReciclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        holder.usernameText.setText(model.getUserName());
        holder.phoneText.setText(model.getPhone());
        if(model.getUserId().equals(FirbaseUtil.currentUsersId()))
            holder.usernameText.setText(model.getUserName() + "(Me)");
//holder.itemView.setOnClickListener(v -> {
//    Intent intent = new Intent(context, ChatActivity.class);
//    AndroidUtil.passUserModelAsIntent(intent,model);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    context.startActivity(intent);
//
//
//});
    }


    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     //   View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return null;// new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
TextView usernameText;
TextView phoneText;
ImageView profilePic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
         //  usernameText = itemView.findViewById(R.id.user_name_text);
         //  phoneText = itemView.findViewById(R.id.phone_text);
         //  profilePic = itemView.findViewById(R.id.profile_pic_image_view);

        }
    }
}
