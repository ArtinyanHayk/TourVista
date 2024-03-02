package com.example.destination;

import static com.example.destination.utils.FirbaseUtil.logout;
import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.destination.model.UserModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.FirbaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MyAccount extends AppCompatActivity {
    ImageView profilePic;
    EditText usernameInput;
    EditText phonrInput;
    Button updateProfileBtn;
    ProgressBar progressBar;
    TextView logoutBtn;
    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLaunch;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        try{
            imagePickLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                selectedImageUri = data.getData();
                                AndroidUtil.setProfilPic(getApplicationContext(), selectedImageUri, profilePic);
                            }
                        }
                    }
            );
            profilePic = findViewById(R.id.profile_image_view);
            usernameInput = findViewById(R.id.profile_username);
            phonrInput = findViewById(R.id.profile_phone);
            progressBar = findViewById(R.id.profile_progress_bar);
            updateProfileBtn = findViewById(R.id.profile_update_btn);
            logoutBtn = findViewById(R.id.logout_btn);

            getUserData();
            updateProfileBtn.setOnClickListener(v -> {
                updateBtnClick();

            });
            logoutBtn.setOnClickListener(v -> {
                logout();
                Intent intent = new Intent(getApplicationContext(), Start_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            });
            profilePic.setOnClickListener(v -> {
                ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512).
                        createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLaunch.launch(intent);
                                return null;
                            }
                        });
            });
        }
        catch(Exception e){

        }
    }
    void updateBtnClick() {
        String newUsername = usernameInput.getText().toString();
        if (newUsername.isEmpty() || newUsername.length() < 3) {
            usernameInput.setError("Username length should be  at least 3 chars");
            return;
        }
        currentUserModel.setUserName(newUsername);
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
    void updateToFirestore() {
        FirbaseUtil.currentUsersDetails().set(currentUserModel).addOnCompleteListener(task -> {
            setInProgress(false);
            if (task.isSuccessful()) {
                AndroidUtil.showToast(getApplicationContext(), "Updated successfully");
            } else {
                AndroidUtil.showToast(getApplicationContext(), "Updat failed");
            }

        });

    }
    void getUserData() {
        FirbaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilPic(getApplicationContext(),uri,profilePic);
                    }
                });
        FirbaseUtil.currentUsersDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUserModel = task.getResult().toObject(UserModel.class);
            usernameInput.setText(currentUserModel.getUserName());
            phonrInput.setText(currentUserModel.getPhone());

        });
    }
    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            updateProfileBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            updateProfileBtn.setVisibility(View.VISIBLE);
        }
    }
}