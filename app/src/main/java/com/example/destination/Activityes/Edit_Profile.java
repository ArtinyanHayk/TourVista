package com.example.destination.Activityes;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.destination.Fragments.ProfileFragment;
import com.example.destination.ImageCropper.CropperActivity;
import com.example.destination.R;
import com.example.destination.model.PostImageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Edit_Profile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 101;

    private TextView nameTv, followingCountTv, followersCountTv, postCountTv;
    private ImageView profileImage, editProfileButton;
    private Button followBtn;
    private Button save;
    private FirebaseUser user;
    private RelativeLayout followLayout;
    private LinearLayout countLayout;
    private boolean isMyProfile = true;
    private FirebaseAuth auth;
    private ActivityResultLauncher<String> mGetContent;
    private Uri selectedImageUri;
    private NestedScrollView scrollView;
    private EditText username;
    private ProgressDialog progressDialog;
    private List<String> following;
    private List<String> followers;
    private int postCount;
    private RelativeLayout profileInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();

        editProfileButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        save.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri);
            } else {
                Toast.makeText(Edit_Profile.this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            String result = data.getStringExtra("RESULT");
            if (result != null) {
                selectedImageUri = Uri.parse(result);
                profileImage.setImageURI(selectedImageUri);
            } else {
                Toast.makeText(Edit_Profile.this, "Image not selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(Uri uri) {
        progressDialog.show();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("ProfileImages").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        imageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    String imageURL = uri1.toString();
                    updateUserProfileImage(uri1);
                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Edit_Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserProfileImage(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("imageURL", uri.toString());
                    map.put("userName",username.getText().toString());

                    FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                            .update(map)
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                Toast.makeText(Edit_Profile.this, "Обновление прошло успешно", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(Edit_Profile.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Edit_Profile.this, "Ошибка при обновлении профиля", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void init() {
        scrollView = findViewById(R.id.scrollView);
        username = findViewById(R.id.nameTv);
        followingCountTv = findViewById(R.id.followingCountTv);
        followersCountTv = findViewById(R.id.folowersCountTv);
        profileImage = findViewById(R.id.profile_image);
        followBtn = findViewById(R.id.followbtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        countLayout = findViewById(R.id.countLayout);
        editProfileButton = findViewById(R.id.changeProfilPic);
        postCountTv = findViewById(R.id.postsTv);
        profileInfoLayout = findViewById(R.id.profile_info);
        save = findViewById(R.id.save);

        progressDialog = new ProgressDialog(Edit_Profile.this);
        progressDialog.setMessage("Updating profile...");
        progressDialog.setCancelable(false);
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                Intent intent = new Intent(Edit_Profile.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        loadBasicData();
    }

    @SuppressLint("RestrictedApi")
    private void loadBasicData() {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());

        userRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null && value.exists()) {
                String userName = value.getString("userName");
                following = (List<String>) value.get("following");
                followers = (List<String>) value.get("followers");
                String profileURL = value.getString("imageURL");

                if (userName != null) {
                    username.setHint(userName);
                }
                Glide.with(getApplicationContext())
                        .load(profileURL)
                        .placeholder(R.drawable.ic_person)
                        .timeout(6500)
                        .into(profileImage);
                DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
                int screenWidth = displayMetrics.widthPixels;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.map_icon, options);
                float aspectRatio = options.outWidth / (float) options.outHeight;
                int calculatedHeight = (int) (screenWidth / aspectRatio);
                ViewGroup.LayoutParams layoutParams = profileImage.getLayoutParams();
                layoutParams.height = calculatedHeight;
                profileImage.setLayoutParams(layoutParams);
            }
        });
    }
}