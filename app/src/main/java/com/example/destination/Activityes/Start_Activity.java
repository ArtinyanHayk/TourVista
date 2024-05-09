package com.example.destination.Activityes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.destination.Chat.Chat;
import com.example.destination.R;
import com.example.destination.model.UserModel;
import com.example.destination.utils.FirbaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Start_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (FirbaseUtil.isLoggedIn() &&  getIntent().getExtras() != null && getIntent().getExtras().getString("userId") != null) {
                        //from notification
                        String userId = getIntent().getExtras().getString("userId");
                        FirbaseUtil.allUsersCollectionReference().document(userId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                UserModel model = task.getResult().toObject(UserModel.class);
                                Intent intent = new Intent(Start_Activity.this, Chat.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("username", model.getUserName());
                                intent.putExtra("person2_id", userId);
                                intent.putExtra("profilePic", model.getImageURL());
                                startActivity(intent);
                                finish();

                            }
                        });
                    } else {
                        if (FirbaseUtil.isLoggedIn()) {
                            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                startActivity(new Intent(Start_Activity.this, MainActivity.class));
                            } else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                startActivity(new Intent(Start_Activity.this, MainActivity.class));

                            }
                        } else {
                            startActivity(new Intent(Start_Activity.this, Registration.class));
                        }

                        finish();

                    }
                }

            },1500);
        }


    }

