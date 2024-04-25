package com.example.destination.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.Chat.Chat;
import com.example.destination.R;
import com.example.destination.model.ChatModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatesonCreateViewHolder> {

    private final List<ChatModel> messageLists;
    private final Context context;

    public ChatAdapter(List<ChatModel> messageLists, Context context) {
        this.messageLists = messageLists;
        this.context = context;
    }


    @NonNull
    @Override
    public ChatesonCreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatesonCreateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatesonCreateViewHolder holder, int position) {
        ChatModel messageList = messageLists.get(position);

        if (messageList.getProfilePic() != null) {
            Glide.with(context.getApplicationContext())
                    .load(messageList.getProfilePic())
                    .placeholder(R.drawable.ic_person)
                    .timeout(6500)
                    .into(holder.profileImage);
        }

        holder.name.setText(messageList.getName());
        holder.lastMessage.setText(messageList.getLastMessage());

        if (messageList.getUnseenMessage() == 0) {
            holder.unseenMessages.setVisibility(View.GONE);
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(String.valueOf(messageList.getUnseenMessage()));
        }

        holder.bindChatClick(messageList);
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
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

        public void bindChatClick(ChatModel messageList) {
            chatLayout.setOnClickListener(v -> openChatActivity(messageList));
        }

        private void openChatActivity(ChatModel messageList) {
            Intent intent = new Intent(context, Chat.class);
            intent.putExtra("name", messageList.getName());
            intent.putExtra("profilPic", messageList.getProfilePic());
            context.startActivity(intent);
        }
    }
}

