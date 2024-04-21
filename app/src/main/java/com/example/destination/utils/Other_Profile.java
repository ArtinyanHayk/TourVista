package com.example.destination.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.destination.R;
import com.example.destination.adapter.ProfileAdapter;
import com.example.destination.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Other_Profile extends AppCompatActivity {
    RecyclerView recyclerView;
    ProfileAdapter adapter;
    UserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        model = new UserModel(
                getIntent().getStringExtra("username"),
                getIntent().getIntExtra("followers", 0),
                getIntent().getIntExtra("following", 0),
                getIntent().getStringExtra("Uid"),
                getIntent().getStringExtra("imageURL"));

        List<UserModel> userList = new ArrayList<>();
        userList.add(model);

        adapter = new ProfileAdapter(this, userList);
        recyclerView.setAdapter(adapter);

    }
}