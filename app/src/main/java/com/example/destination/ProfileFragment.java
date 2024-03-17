package com.example.destination;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.model.PostImageModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileFragment extends Fragment {
    private TextView nameTv, followersCountTv, postCountTv, followingCountTv, ratingCountTv;
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
    private ImageButton editProfileButton;
    ActivityResultLauncher<Intent> imagePickLaunch;
    Uri selectedImageUri;
    ImageView profilePic;

    // Объявление ProgressDialog для отображения индикатора загрузки
    ProgressDialog progressDialog;

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
        return inflater.inflate(R.layout.activity_profile_fragment, container, false);
    }

    @SuppressLint("RestrictedApi")
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

        // Инициализация ProgressDialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Обновление профиля...");
        progressDialog.setCancelable(false);

        imagePickLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilPic(getApplicationContext(), selectedImageUri, profileImage);
                            uploadImage(selectedImageUri);
                        }
                    }
                }
        );

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Profile Fragment", "Кнопка профиля нажата");
                progressDialog.show(); // Показать индикатор загрузки

                ImagePicker.with(getActivity()).cropSquare().compress(512).maxResultSize(512, 512).
                        createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLaunch.launch(intent);
                                return null;
                            }
                        });

            }
        });
    }

    private void uploadImage(Uri uri) {
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Profile Image").child(FirbaseUtil.currentPostsId());
        reference.putFile(uri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        progressDialog.dismiss(); // Скрыть индикатор загрузки

                        if (task.isSuccessful()) {
                            reference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageURL = selectedImageUri.toString();
                                            UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                                            request.setPhotoUri(selectedImageUri);

                                            user.updateProfile(request.build());

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("imageURL", imageURL);
                                            FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                                                    .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getContext(), "Обновление прошло успешно", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(getContext(), "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void init(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        nameTv = view.findViewById(R.id.nameTv);
        followersCountTv = view.findViewById(R.id.folowersCountTv);
        profileImage = view.findViewById(R.id.profile_image);
        followBtn = view.findViewById(R.id.followbtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        countLayout = view.findViewById(R.id.countLayout);
        editProfileButton = view.findViewById(R.id.edit_profileImage);
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
                    Long followers = value.getLong("folowers");
                    String profileURL = value.getString("imageURL");


                    if (userName != null) {
                        nameTv.setText(userName);
                    }

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
    /////////////KAXELOVA ASSHXATM

    private void loadPostsImages() {
        Query query = FirebaseFirestore.getInstance().collection("postImages");

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
                    Glide.with(holder.itemView.getContext().getApplicationContext())
                            .load(model.getImageUrl())
                            .timeout(6500)
                            .into(holder.imageView);
                }
            }
        };
    }

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
}
