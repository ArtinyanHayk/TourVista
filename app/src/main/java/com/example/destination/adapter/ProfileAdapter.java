package com.example.destination.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.R;
import com.example.destination.model.UserModel;
import com.example.destination.model.PostImageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileHolder> {

    private Context context;
    private List<UserModel> list;

    public ProfileAdapter(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ProfileHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        holder.loadPostsImages();

        UserModel userModel = list.get(position);
        holder.nameTv.setText(userModel.getUserName());
        holder.followersCountTv.setText(Integer.toString(userModel.getFolowers()));
        holder.followingCountTv.setText(Integer.toString(userModel.getFolowing()));

        Glide.with(context.getApplicationContext())
                .load(userModel.getImageURL())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProfileHolder extends RecyclerView.ViewHolder {

        private TextView nameTv, followersCountTv, postCountTv, followingCountTv, ratingCountTv;
        private CircleImageView profileImage;
        private Button followBtn;
        private RecyclerView recyclerView;
        private String uid;
        private FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;

        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            followersCountTv = itemView.findViewById(R.id.folowersCountTv);
            profileImage = itemView.findViewById(R.id.profile_image);
            followBtn = itemView.findViewById(R.id.followbtn);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            postCountTv = itemView.findViewById(R.id.postsTv);
            followingCountTv = itemView.findViewById(R.id.followingTv);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));;
        }

        private void loadPostsImages() {
            UserModel userModel = list.get(getAdapterPosition());
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
                        Glide.with(holder.itemView.getContext())
                                .load(model.getImageUrl())
                                .timeout(6500)
                                .into(holder.imageView);
                    }
                }

                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChanged() {
                    super.onDataChanged();
                    adapter.notifyDataSetChanged(); // Notify adapter about data changes
                }
            };

            recyclerView.setAdapter(adapter);
            adapter.startListening(); // Start listening to Firestore data
        }
    }

    public static class PostImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}


