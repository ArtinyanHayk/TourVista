package com.example.destination;

import static com.example.destination.utils.FirbaseUtil.logout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.destination.R;
import com.example.destination.model.UserModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.FirbaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Add_location_Fragment extends Fragment {



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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dusa qcm
        try{
            imagePickLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                selectedImageUri = data.getData();
                                AndroidUtil.setProfilPic(getContext(), selectedImageUri, profilePic);
                            }
                        }
                    }
            );
        }
        catch(Exception e){

        }
    }

    public Add_location_Fragment() {

    }

    @SuppressLint({"CutPasteId", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        profilePic = view.findViewById(R.id.profile_image_view);
        usernameInput = view.findViewById(R.id.profile_username);
        phonrInput = view.findViewById(R.id.profile_phone);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        updateProfileBtn = view.findViewById(R.id.profile_update_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);

        getUserData();
        updateProfileBtn.setOnClickListener(v -> {
            updateBtnClick();

        });
        logoutBtn.setOnClickListener(v -> {
            logout();
            Intent intent = new Intent(getContext(), Start_Activity.class);
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

        return view;
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
                AndroidUtil.showToast(getContext(), "Updated successfully");
            } else {
                AndroidUtil.showToast(getContext(), "Updat failed");
            }

        });

    }


    void getUserData() {
        FirbaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilPic(getContext(),uri,profilePic);
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