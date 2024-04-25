package com.example.destination.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.destination.R;
import com.example.destination.model.MessageModel;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MessagesAdapter extends FirestoreRecyclerAdapter<MessageModel, MessagesAdapter.MessagesViewHolder> {
    private Context context;

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
        }else{
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextview.setText(model.getMessage());
        }
    }
    @Override
    public int getItemCount() {
        return super.getItemCount(); // Ensure the item count is correct
    }



    public static class MessagesViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout,rightChatLayout;
        TextView leftChatTextview,rightChatTextview;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}