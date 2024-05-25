package com.example.destination.Activityes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.destination.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.destination.Chat.Chat;
import com.example.destination.adapter.SearchUserReciclerAdapter;
import com.example.destination.model.ChatModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.FirbaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chat_serach extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<UserModel> filteredList;
    private SearchUserReciclerAdapter adapter;
    private CollectionReference reference;
    public  List<String> following;
    FirebaseUser user;
    CollectionReference chats;
    String currentUserName;
    ChatModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_serach);
        init();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        adapter.onSelected(new SearchUserReciclerAdapter.onSelected() {
            @Override
            public void onSelect(int position, String Uid, String Username, String imageURL, List<String> followers, List<String> following, int posts) {
                filterList("");
                filteredList.clear();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String time = sdf.format(date);



                chats.document(FirbaseUtil.chatId(FirbaseUtil.currentUsersId(),Uid)).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        chatModel = task.getResult().toObject(ChatModel.class);
                        if(chatModel == null){
                            chatModel = new ChatModel(
                                    "",
                                    Timestamp.now(),
                                    "",
                                    Arrays.asList(user.getUid(),Uid)


                            );
                        }
                        chats.document(FirbaseUtil.chatId(FirbaseUtil.currentUsersId(),Uid)).set(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Intent intent = new Intent(Chat_serach.this, Chat.class);
                                    intent.putExtra("username", Username);
                                    intent.putExtra("person2_id", Uid);
                                    intent.putExtra("profilePic", imageURL);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(Chat_serach.this, "check your connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });


            }
        });
    }
    private void init() {
        recyclerView = findViewById(R.id.search_resycler_view);
        searchView = findViewById(R.id.searchView);
        filteredList = new ArrayList<>();
        adapter = new SearchUserReciclerAdapter(filteredList, Chat_serach.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Chat_serach.this));
        reference = FirebaseFirestore.getInstance().collection("users");
        chats = FirebaseFirestore.getInstance().collection("chats");
        user = FirebaseAuth.getInstance().getCurrentUser();
        following = new ArrayList<>();
        reference.document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                following = (List<String>) documentSnapshot.get("following");
                currentUserName = (String) documentSnapshot.get("userName");
            }
        });
        searchView.clearFocus();
        searchView.requestFocus();
    }

    private void filterList(String text) {
        filteredList.clear();

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("search", "error: " + error);
                    return;
                }
                if (value == null || value.isEmpty()) {
                    return;
                }

                for (QueryDocumentSnapshot snapshot : value) {
                    if (!snapshot.exists()) {
                        continue;
                    }
                    UserModel model = snapshot.toObject(UserModel.class);

                    if (model.getUserName().toLowerCase().contains(text.toLowerCase()) && following.contains(model.getUserId())) {


                        filteredList.add(new UserModel(
                                model.getPhone(),
                                model.getUserName(),
                                model.getCreatedTimesetap(),
                                model.getUserId(),
                                model.getFollowers(),
                                model.getFollowing(),
                                model.getImageURL(),
                                model.getonline()

                        ));
                    }
                }
                if (filteredList.isEmpty()) {
                    //Toast.makeText(Chat_serach.this, "Нет данных", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}