package com.example.destination;

import static com.example.destination.utils.FirbaseUtil.logout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileFragment extends Fragment {
<<<<<<< Updated upstream
    private TextView nameTv, statusTv, followersCountTv, postCountTv, folowingCountTv, reitingCountTv;
    private CircleImageView profileImage;
    private Button followBtn;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private RelativeLayout followLayout;
    private LinearLayout countLayout;
    boolean isMyProfile = true;
    FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;
    String uid;
    FirebaseAuth auth;
=======

>>>>>>> Stashed changes

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ProfileFragment() {
    }

    @SuppressLint({"CutPasteId", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
<<<<<<< Updated upstream
        return inflater.inflate(R.layout.activity_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        if (isMyProfile) {
            followBtn.setVisibility(View.GONE);
            countLayout.setVisibility(View.VISIBLE);
        } else {
            followBtn.setVisibility(View.VISIBLE);
            countLayout.setVisibility(View.GONE);
        }

        loadBasicData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        loadPostsImages();
        recyclerView.setAdapter(adapter);
    }

    public void init(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        nameTv = view.findViewById(R.id.nameTv);
        followersCountTv = view.findViewById(R.id.folowersCountTv);
        profileImage = view.findViewById(R.id.profile_image_view);
        followBtn = view.findViewById(R.id.followbtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        countLayout = view.findViewById(R.id.countLayout);
    }

    public void loadBasicData() {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value.exists()) {
                    String userName = value.getString("userName");
                    Long followers = value.getLong("followers");
                    String profileURL = value.getString("profileImage");
=======


        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);


        return view;
    }

>>>>>>> Stashed changes

                    if (userName != null) {
                        nameTv.setText(userName);
                    }

<<<<<<< Updated upstream
                    if (followers != null) {
                        followersCountTv.setText(String.valueOf(followers));
                    }

                    if (profileURL != null) {
                        Glide.with(requireContext().getApplicationContext())
                                .load(profileURL)
                                .placeholder(R.drawable.ic_person)
                                .timeout(6500)
                                .into(profileImage);
                    }
                }
            }
        });
    }

    private void loadPostsImages() {
        uid = isMyProfile ? user.getUid() : "";

        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(uid);
        Query query = reference.collection("Images");
=======
>>>>>>> Stashed changes

        FirestoreRecyclerOptions<PostImageModel> options = new FirestoreRecyclerOptions.Builder<PostImageModel>()
                .setQuery(query, PostImageModel.class)
                .build();

<<<<<<< Updated upstream
        adapter = new FirestoreRecyclerAdapter<PostImageModel, PostImageHolder>(options) {
            @NonNull
            @Override
            public PostImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_items, parent, false);
                return new PostImageHolder(view);
            }
=======
>>>>>>> Stashed changes

            @Override
            protected void onBindViewHolder(@NonNull PostImageHolder holder, int position, @NonNull PostImageModel model) {
                if (model.getImageUrl() != null) {
                    Glide.with(holder.itemView.getContext().getApplicationContext())
                            .load(model.getImageUrl())
                            .timeout(6500)
                            .into(holder.imageView);
                }
            }
        };
    }

<<<<<<< Updated upstream
    private static class PostImageHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
=======



>>>>>>> Stashed changes
}
