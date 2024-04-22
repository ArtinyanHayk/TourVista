package com.example.destination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.destination.adapter.ProfileAdapter;
import com.example.destination.model.PostImageModel;
import com.example.destination.model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Other_Profile extends AppCompatActivity {
    private TextView nameTv, followersCountTv, postCountTv, followingCountTv;
    private ImageView profileImage;
    private Button followBtn;
    private RecyclerView recyclerView;
    private String uid;
    private FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        init();

        userModel = new UserModel(
                getIntent().getStringExtra("username"),
                getIntent().getIntExtra("followers", 0),
                getIntent().getIntExtra("following", 0),
                getIntent().getStringExtra("Uid"),
                getIntent().getStringExtra("imageURL"));

        nameTv.setText(userModel.getUserName());
        followersCountTv.setText(Integer.toString(userModel.getFolowers()));
        followingCountTv.setText(Integer.toString(userModel.getFolowing()));

        Glide.with(this)
                .load(userModel.getImageURL())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(profileImage);

        loadPostsImages();
    }

    private void init() {
        nameTv = findViewById(R.id.nameTv);
        followersCountTv = findViewById(R.id.folowersCountTv);
        profileImage = findViewById(R.id.profile_image);
        followBtn = findViewById(R.id.followbtn);
        recyclerView = findViewById(R.id.recyclerView);
        postCountTv = findViewById(R.id.postsTv);
        followingCountTv = findViewById(R.id.followingTv);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                adapter.notifyDataSetChanged(); // Notify adapter about data changes
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
