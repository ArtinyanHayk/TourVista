package com.example.destination.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.destination.Activityes.Other_Profile;
import com.example.destination.Activityes.search_Activity;
import com.example.destination.R;
import com.example.destination.adapter.SearchUserReciclerAdapter;
import com.example.destination.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<UserModel> filteredList;
    private SearchUserReciclerAdapter adapter;
    private CollectionReference reference;
    public  List<String> following;
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

            }
        });
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.search_resycler_view);
        searchView = view.findViewById(R.id.searchView);
        filteredList = new ArrayList<>();
        adapter = new SearchUserReciclerAdapter(filteredList, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reference = FirebaseFirestore.getInstance().collection("users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        following = new ArrayList<>();
         reference.document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
             if(documentSnapshot.exists()){
                 following = (List<String>) documentSnapshot.get("following");
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
                                model.getStatus()

                        ));
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(getContext(), "Нет данных", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}