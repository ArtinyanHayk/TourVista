package com.example.destination;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.destination.adapter.SearchUserReciclerAdapter;
import com.example.destination.adapter.ViewPagerAdapter;
import com.example.destination.model.UserModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class search_Activity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;
    List<UserModel> filteredList;

    SearchUserReciclerAdapter adapter;
    SearchView searchView;
    CollectionReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_resycler_view);
        searchView = findViewById(R.id.searchView);

        recyclerView.setHasFixedSize(true);
        filteredList = new ArrayList<>();
        adapter = new SearchUserReciclerAdapter(filteredList, search_Activity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseFirestore.getInstance().collection("users");

        searchView.clearFocus();
        searchView.requestFocus();

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

       adapter.onSelected(new SearchUserReciclerAdapter.onSelected() {
           @Override
           public void onSelect(int position, String Uid, String Username, String imageURL, int followers, int following, int posts) {
               ProfileFragment fragment = new ProfileFragment();

               Bundle bundle = new Bundle();
               bundle.putString("Uid", Uid);
               bundle.putString("Username", Username);
               bundle.putString("ImageURL", imageURL);
               bundle.putInt("Followers", followers);
               bundle.putInt("Following", following);
               bundle.putInt("Posts", posts);
               bundle.putString("Fragment", "Profile");

               fragment.setArguments(bundle);


           }
       });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterlist(newText);
                return true;
            }
        });
    }

    private void filterlist(String text) {
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

                    if (model.getUserName().toLowerCase().contains(text.toLowerCase())) {
                        Toast.makeText(search_Activity.this, model.getUserName(), Toast.LENGTH_SHORT).show();

                        filteredList.add(new UserModel(
                                model.getPhone(),
                                model.getUserName(),
                                model.getCreatedTimesetap(),
                                model.getUserId(),
                                model.getFolowers(),
                                model.getFolowing(),
                                model.getImageURL(),
                                model.getStatus()
                        ));
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(search_Activity.this, "Нет данных", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}