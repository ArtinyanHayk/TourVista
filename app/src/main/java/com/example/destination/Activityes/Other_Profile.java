package com.example.destination.Activityes;
//
///
///
//
//logout Token delate Chat video
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.PostImageModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.BaseApplication;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Other_Profile  extends BaseApplication {
    private TextView nameTv, followersCountTv, postCountTv, followingCountTv;
    private ImageView profileImage;
    private LottieAnimationView followBtn;
    private RecyclerView recyclerView;
    private String uid;
    private FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;
    private UserModel userModel;
    private FirebaseUser user;
    private List<String> followersList, followingsList;
    private FirebaseFirestore db;
    private DocumentReference otherUser, me;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        init();

        loadAccountData();
        loadPostsImages();
        if (followersList.contains(user.getUid())) {
            followBtn.setProgress(1f);  // Set to the end of the animation
        } else {
            followBtn.setProgress(0f);  // Set to the start of the animation
        }

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followersList.contains(user.getUid())) {
                    // User is currently following; unfollow them
                    followersList.remove(user.getUid());
                    followingsList.remove(userModel.getUserId());

                    // Play the animation in reverse
                    followBtn.cancelAnimation();
                    followBtn.setProgress(0f);
                } else {
                    // User is not currently following; follow them
                    followersList.add(user.getUid());
                    followingsList.add(userModel.getUserId());

                    // Play the animation normally
                    followBtn.setSpeed(1f);
                    followBtn.playAnimation();
                }

                // Prepare the data to update in Firestore
                Map<String, Object> followers = new HashMap<>();
                followers.put("followers", followersList);

                Map<String, Object> followings = new HashMap<>();
                followings.put("following", followingsList);

                // Run the Firestore transaction
                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        transaction.update(otherUser, followers);
                        transaction.update(me, followings);
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Followed!");
                        followersCountTv.setText(Integer.toString(followersList.size()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(TAG, "Error Following", e);

                        // Revert changes in the lists if the transaction fails
                        if (followersList.contains(user.getUid())) {
                            followersList.remove(user.getUid());
                            followingsList.remove(userModel.getUserId());
                            followBtn.setProgress(1f);

                        } else {
                            followersList.add(user.getUid());
                            followingsList.add(userModel.getUserId());
                            followBtn.setProgress(0f);
                        }
                        Toast.makeText(Other_Profile.this, "Failed to follow/unfollow", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadAccountData() {
        if (userModel.getUserId().equals(user.getUid())) {
            followBtn.setVisibility(View.INVISIBLE);
        }

        nameTv.setText(userModel.getUserName());
        followersCountTv.setText(Integer.toString(followersList.size()));
        followingCountTv.setText(Integer.toString(userModel.getFollowing().size()));



        Glide.with(this)
                .load(userModel.getImageURL())
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


    private void init() {
        nameTv = findViewById(R.id.nameTv);
        followersCountTv = findViewById(R.id.folowersCountTv);
        profileImage = findViewById(R.id.profile_image);
        followBtn = findViewById(R.id.followbtn);
        recyclerView = findViewById(R.id.recyclerView);
        postCountTv = findViewById(R.id.postsTv);
        followingCountTv = findViewById(R.id.followingCountTv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        user = FirebaseAuth.getInstance().getCurrentUser();

        userModel = new UserModel(
                getIntent().getStringExtra("username"),
                getIntent().getStringArrayListExtra("followers"),
                getIntent().getStringArrayListExtra("following"),
                getIntent().getStringExtra("Uid"),
                getIntent().getStringExtra("imageURL"));

        db = FirebaseFirestore.getInstance();
        otherUser = db.collection("users").document(userModel.getUserId());
        me = db.collection("users").document(user.getUid());

        followersList = new ArrayList<>(userModel.getFollowers());
        followingsList = new ArrayList<>(userModel.getFollowing());

        otherUser.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                followersList = (List<String>) documentSnapshot.get("followers");
            }
        });

        me.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                followingsList = (List<String>) documentSnapshot.get("following");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void loadPostsImages() {
        uid = userModel.getUserId();

        Query query = FirebaseFirestore.getInstance().collection("userPosts").whereEqualTo("uid", uid);

        FirestoreRecyclerOptions<PostImageModel> options = new FirestoreRecyclerOptions.Builder<PostImageModel>()
                .setQuery(query, PostImageModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostImageModel, PostImageHolder>(options) {
            @NonNull
            @Override
            public PostImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_items, parent, false);
                return new PostImageHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostImageHolder holder, int position, @NonNull PostImageModel model) {
                if (model.getImageUrl() != null) {
                    Glide.with(Other_Profile.this)
                            .load(model.getImageUrl())
                            .timeout(6500)
                            .into(holder.imageView);
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                adapter.notifyDataSetChanged();
                postCountTv.setText(String.valueOf(adapter.getItemCount()));
                // Notify adapter about data changes
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening(); // Start listening to Firestore data
    }

    public static class PostImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}