package com.example.destination;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.destination.model.HomeModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.FirbaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Add_Post extends AppCompatActivity {
    Uri selectedImageUri;
    ActivityResultLauncher<Intent> imagePickLaunch;
    ImageView postpick;
    EditText description;
    EditText tags;
    ProgressBar progressBar;
    ImageButton post;
    HomeModel currentPostModel;

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndroidUtil.setPostPic(getApplicationContext(), selectedImageUri, postpick);
                        }
                    }
                });
        description = findViewById(R.id.description);
        tags = findViewById(R.id.add_tags);
        tags = findViewById(R.id.add_tags);
        postpick = findViewById(R.id.post_image);
        progressBar = findViewById(R.id.add_progress_bar);
        post = findViewById(R.id.add_post);
        postpick.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512).
                    createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLaunch.launch(intent);
                            return null;
                        }
                    });
        });
        void updateBtnClick() {
            String newUsername = description.getText().toString();
            if (newUsername.isEmpty() || newUsername.length() < 3) {
                description.setError("Username length should be  at least 3 chars");
                return;
            }
            currentPostModel.setUserName(newUsername);
            setInProgress(true);
            if (selectedImageUri != null) {
                FirbaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri).addOnCompleteListener(
                        task -> {
                            updateToFirestore();
                        }
                );
            } else {
                updateToFirestore();
            }


        }
    }
}