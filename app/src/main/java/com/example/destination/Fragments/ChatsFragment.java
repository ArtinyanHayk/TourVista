package com.example.destination.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.destination.Activityes.Chat_serach;
import com.example.destination.R;

import com.example.destination.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageButton searchView;
    private List<ChatModel> messagesLists = new ArrayList<>();
    private int unseenMessage = 0;
    private String lastMessage = "", chatKey = "";
    private boolean dataSet = false;

    private DatabaseReference databaseReference;
   // private MessageAdapter adapter;
    private CollectionReference reference;

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
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.search_resycler_view);
        searchView = view.findViewById(R.id.search_follower_btn);
       // adapter = new MessageAdapter(messagesLists, getContext());
       // recyclerView.setHasFixedSize(true);
       // recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reference = FirebaseFirestore.getInstance().collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "12", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Chat_serach.class));
            }
        });


    }
}
