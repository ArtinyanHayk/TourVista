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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.destination.R;

import com.example.destination.adapter.MessagesAdapter;
import com.example.destination.model.ChatModel;
import com.example.destination.model.MessageModel;
import com.example.destination.utils.FirbaseUtil;
import com.example.destination.utils.MemoryData;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
public class Chat extends AppCompatActivity {
    int generatechatkey;
    EditText messageEditText;
    String UserEmail = "";
    String chatId;
    FirebaseUser user;
    String id2;
    ChatModel chatModel;
    MessageModel messageModel;
    MessagesAdapter adapter;
    ImageView sendBtn;
    ImageView backBtn;
    TextView name;
    ImageView bacBtn;
    CircleImageView profilePic;
    RecyclerView recyclerView;

    CollectionReference messages;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bacBtn = findViewById(R.id.back_btn);
        name = findViewById(R.id.name);
        messageEditText = findViewById(R.id.messageEditTxt);
        profilePic = findViewById(R.id.profilePic);
        sendBtn = findViewById(R.id.sendBtn);
        backBtn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.recyclerView);

        UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        user = FirebaseAuth.getInstance().getCurrentUser();

        bacBtn.setOnClickListener(v -> finish());
        final String getname = getIntent().getStringExtra("username");
        id2 = getIntent().getStringExtra("person2_id");
        final String getProfilPic = getIntent().getStringExtra("profilPic");

        Glide.with(Chat.this.getApplicationContext())
                .load(getProfilPic)
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(profilePic);
        name.setText(getname);

        chatModel = new ChatModel("", Arrays.asList(user.getUid(), id2), null);

        chatId = FirbaseUtil.chatId(user.getUid(), id2);
        getOrCreateChatroomModel();
        setupChatRecyclerView();

        sendBtn.setOnClickListener(v -> {
            final String currentTimeStump = String.valueOf(System.currentTimeMillis()).substring(0, 10);
            String gettxtMessage = messageEditText.getText().toString();
            if (gettxtMessage.isEmpty()) {
                return;
            }
            try {
                MemoryData.saveLastMsgTS(currentTimeStump, chatId, Chat.this);
                SendMessage(gettxtMessage, user.getUid());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void setupChatRecyclerView(){
        Query query = FirebaseFirestore.getInstance().collection("chats")
                .document(chatId).collection("messages")
                .orderBy("time",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query,MessageModel.class).build();

        adapter = new MessagesAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    void SendMessage(String message, String id) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(date);
        messageModel = new MessageModel(message, id, Timestamp.now().toString());

        chatModel.setLastMessage(message);
        chatModel.setLastMessageTime(Timestamp.now().toString());

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
    void getOrCreateChatroomModel(){
        FirbaseUtil.getChatReference(chatId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatModel = task.getResult().toObject(ChatModel.class);
                if(chatModel==null){
                    //first time chat
                    chatModel = new ChatModel(
                            "",
                            Arrays.asList(FirbaseUtil.currentUsersId(),id2),
                            Timestamp.now().toString()

                    );
                    FirbaseUtil.getChatReference(chatId).set(chatModel);
                }
            }
        });
    }

}