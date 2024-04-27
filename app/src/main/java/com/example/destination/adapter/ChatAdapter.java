package com.example.destination.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.Activityes.Chat_serach;
import com.example.destination.Chat.Chat;
import com.example.destination.R;
import com.example.destination.model.ChatModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ChatesonCreateViewHolder> {
    private Context context;
    String otherId = "";
    boolean repeat = false;

    public ChatAdapter(FirestoreRecyclerOptions<ChatModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ChatesonCreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ChatesonCreateViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatesonCreateViewHolder holder, int position, @NonNull ChatModel model) {
        if(!otherId.equals(FirbaseUtil.getOtherUserId(model.getMembersId()))){
            otherId = FirbaseUtil.getOtherUserId(model.getMembersId());
            otherId =  FirbaseUtil.getOtherUserId(model.getMembersId());
            FirbaseUtil.getOtherUserChat(model.getMembersId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserModel userModel = task.getResult().toObject(UserModel.class);

                    if(model.getLastMsgSenderId().equals(userModel.getUserId()) ){
                        holder.lastMessage.setText(model.getLastMessage());
//
                    }else{
                        Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
                        holder.lastMessage.setText("Me:" + model.getLastMessage());
                    }
//

                    holder.name.setText(userModel.getUserName());
                    if (userModel.getImageURL() != null) {
                        Glide.with(context.getApplicationContext())
                                .load(userModel.getImageURL())
                                .placeholder(R.drawable.ic_person)
                                .timeout(6500)
                                .into(holder.profileImage);
                        Toast.makeText(context, "nonull", Toast.LENGTH_SHORT).show();
                    }

                    holder.itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(context, Chat.class);
                        intent.putExtra("username", userModel.getUserName());
                        intent.putExtra("person2_id",userModel.getUserId());
                        intent.putExtra("profilePic", userModel.getImageURL());
                        intent.putExtra("status",userModel.getonline());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    });

                }
            });

        }


    }
    @Override
    public int getItemCount() {

        return super.getItemCount(); // Ensure the item count is correct
    }



    class ChatesonCreateViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView profileImage;
        private final TextView name, lastMessage, unseenMessages;
        private final LinearLayout chatLayout;

        public ChatesonCreateViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessage);
            chatLayout = itemView.findViewById(R.id.chat_layout);
        }

      //  public void onClick(int position, String uid, String profilePic, String name) {
      //      chatLayout.setOnClickListener(new View.OnClickListener() {
      //          @Override
      //          public void onClick(View v) {
      //              onPressed.onPress(position, uid, profilePic, name);
      //          }
      //      });
      //  }


    }

}




