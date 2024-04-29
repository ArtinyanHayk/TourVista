package com.example.destination.utils;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class BaseApplication extends AppCompatActivity {
    boolean online;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirbaseUtil.Online(true);

    }

    @Override
    protected void onPause() {
        super.onPause();

            FirbaseUtil.Online(false);

    }

    @Override
    protected void onResume() {
        super.onResume();

            FirbaseUtil.Online(true);


    }
}
