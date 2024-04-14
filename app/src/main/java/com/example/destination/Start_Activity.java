package com.example.destination;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.destination.utils.FirbaseUtil;
import com.google.firebase.auth.FirebaseAuth;

public class Start_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(Start_Activity.this,MainActivity.class));
                }else{
                    startActivity(new Intent(Start_Activity.this,Registration.class));
                }

                finish();

            }
        },1500);
    }
}