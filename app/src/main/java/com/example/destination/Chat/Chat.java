package com.example.destination.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.adapter.MessagesAdapter;
import com.example.destination.model.ChatModel;
import com.example.destination.model.MessageModel;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    EditText messageEditText;
    String chatId;
    FirebaseUser user;
    String id2;
    ChatModel chatModel;
    MessagesAdapter adapter;
    ImageView sendBtn;
    TextView name;
    ImageView bacBtn;
    CircleImageView profilePic;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bacBtn = findViewById(R.id.back_btn);
        name = findViewById(R.id.name);
        messageEditText = findViewById(R.id.messageEditTxt);
        profilePic = findViewById(R.id.profilePic);
        sendBtn = findViewById(R.id.sendBtn);
        recyclerView = findViewById(R.id.recyclerView);

        user = FirebaseAuth.getInstance().getCurrentUser();

        bacBtn.setOnClickListener(v -> finish());
        final String getname = getIntent().getStringExtra("username");
        id2 = getIntent().getStringExtra("person2_id");
        final String getProfilPic = getIntent().getStringExtra("profilePic");

        Glide.with(this)
                .load(getProfilPic)
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(profilePic);
        name.setText(getname);

        chatId = FirbaseUtil.chatId(user.getUid(), id2);
        getOrCreateChatroomModel();
        setupChatRecyclerView();

        sendBtn.setOnClickListener(v -> {
            String gettxtMessage = messageEditText.getText().toString();
            if (gettxtMessage.isEmpty()) {
                return;
            }
            try {
                SendMessage(gettxtMessage, user.getUid());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void setupChatRecyclerView() {
        Query query = FirbaseUtil.getChatMessageReference(chatId)
                .orderBy("time", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query,MessageModel.class).build();

        adapter = new MessagesAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void SendMessage(String message, String id) throws IOException {
        Timestamp timestamp = Timestamp.now();

        MessageModel messageModel = new MessageModel(message, id, com.google.firebase.Timestamp.now());
        chatModel.setLastMessage(message);
        chatModel.setLastMessageTime(com.google.firebase.Timestamp.now());
        chatModel.setLastMsgSenderId(user.getUid());

        FirbaseUtil.getChatReference(chatId).set(chatModel).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "Check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        FirbaseUtil.getChatMessageReference(chatId).add(messageModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageEditText.setText("");
            }
        });
    }

    void getOrCreateChatroomModel() {
        FirbaseUtil.getChatReference(chatId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatModel = task.getResult().toObject(ChatModel.class);
                if (chatModel == null) {
                    // First time chat
                    chatModel = new ChatModel(
                            "",
                            com.google.firebase.Timestamp.now(),
                            "",
                            Arrays.asList(FirbaseUtil.currentUsersId(), id2)


                    );
                    FirbaseUtil.getChatReference(chatId).set(chatModel);
                }
            }
        });
    }
}
