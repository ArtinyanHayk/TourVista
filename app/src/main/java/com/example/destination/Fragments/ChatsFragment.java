package com.example.destination.Fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.destination.Activityes.Chat_serach;
import com.example.destination.Chat.Chat;
import com.example.destination.R;

import com.example.destination.adapter.ChatAdapter;
import com.example.destination.adapter.MessagesAdapter;
import com.example.destination.model.ChatModel;
import com.example.destination.model.MessageModel;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageButton searchView;
    private List<ChatModel> messagesLists;
    private int unseenMessage = 0;
    private String lastMessage = "", chatKey = "";
    private boolean dataSet = false;
    String profilePic, name, id;

    private DatabaseReference databaseReference;
    private ChatAdapter adapter;
    private CollectionReference reference;
    FirebaseUser user;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "12", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Chat_serach.class));
            }
        });


    }


    private void init(View view) {
        recyclerView = view.findViewById(R.id.chats_recycler_view);
        searchView = view.findViewById(R.id.search_follower_btn);
        messagesLists = new ArrayList<>();


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance().collection("chats");
     //   reference.addSnapshotListener((value, error) -> {
     //       if (error != null) {
     //           Log.e(TAG, "Listen failed.", error);
     //           return;
     //       }
//
     //       for (DocumentChange dc : value.getDocumentChanges()) {
     //           switch (dc.getType()) {
     //               case REMOVED:
     //                   // Обработка удаленного документа чата
     //                   String removedChatId = dc.getDocument().getId();
     //                   // Удалите соответствующий элемент из списка чатов вашего приложения
     //                   removeChatItem(removedChatId);
     //                   break;
     //               // Другие обработчики изменений, если необходимо
     //           }
     //       }
     //   });

                Query query = FirbaseUtil.allChatCollectionReference()
                        .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                        .whereArrayContains("membersId",user.getUid());

                FirestoreRecyclerOptions<ChatModel> options = new FirestoreRecyclerOptions.Builder<ChatModel>()
                        .setQuery(query, ChatModel.class).build();

                adapter = new ChatAdapter(options, getContext());
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }







//    private void removeChatItem(String chatId) {
//        for (int i = 0; i < chatList.size(); i++) {
//            if (chatList.get(i).getId().equals(chatId)) {
//                chatList.remove(i);
//                adapter.notifyItemRemoved(i);
//                break;
//            }
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }





    @Override
    public void onPause() {
        super.onPause();
        adapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.startListening();
    }


}

