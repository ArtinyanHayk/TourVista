package com.example.destination.Fragments;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.Activityes.Edit_post;
import com.example.destination.Activityes.MainActivity;
import com.example.destination.ImageCropper.CropperActivity;
import com.example.destination.R;
import com.example.destination.model.PostImageModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.yalantis.ucrop.UCrop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 101;

    private TextView nameTv, followingCountTv,followersCountTv, postCountTv;
    private ImageView profileImage;
    private Button followBtn;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private RelativeLayout followLayout;
    private LinearLayout countLayout;
    private boolean isMyProfile = true;
    private FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;
    private FirebaseAuth auth;
   // private ImageButton editProfileButton;
    private ActivityResultLauncher<String> mGetContent;
    private Uri selectedImageUri;
    private NestedScrollView scrollView;

    private ProgressDialog progressDialog;
    public  List<String> following;
    public  List<String> followers;
    private int postCount;
    RelativeLayout profileInfoLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupUI();

      //  editProfileButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                Intent intent = new Intent(getActivity(), CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            String result = data.getStringExtra("RESULT");
            if (result != null) {
                Uri resultUri = Uri.parse(result);
                uploadImage(resultUri);
            } else {
                Toast.makeText(getContext(), "Изображение не выбрано", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

                    FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                            .update(map)
                            .addOnSuccessListener(aVoid -> {
                                loadBasicData();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Обновление прошло успешно", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Ошибка при обновлении профиля", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void init(View view) {
        scrollView = view.findViewById(R.id.scrollView);
        nameTv = view.findViewById(R.id.nameTv);
        followingCountTv = view.findViewById(R.id.followingCountTv);
        followersCountTv = view.findViewById(R.id.folowersCountTv);
        profileImage = view.findViewById(R.id.profile_image);
        followBtn = view.findViewById(R.id.followbtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        countLayout = view.findViewById(R.id.countLayout);
     //   editProfileButton = view.findViewById(R.id.edit_profileImage);
        postCountTv = view.findViewById(R.id.postsTv);
        profileInfoLayout = view.findViewById(R.id.profile_info);


    }
////////////////////////////////
    /////////////////////
    ////////////////
    /////////ES INCHA
    private void setupUI() {
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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Обновление профиля...");
        progressDialog.setCancelable(false);
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
                String profileURL = value.getString("imageURL");;
///////////////
                ///////////
                //////////Erb follow es anm dusa qcm____following voch te followers
                if (userName != null) {
                    nameTv.setText(userName);
                }
                if (following != null) {
                    followingCountTv.setText(Integer.toString(following.size()));
                }
                if (followers != null) {
                    followersCountTv.setText(Integer.toString(followers.size()));
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

    private void loadPostsImages() {
        Query query = FirebaseFirestore.getInstance().collection("userPosts").whereEqualTo("uid", user.getUid()).orderBy("timestamp",Query.Direction.ASCENDING);


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

                    postCount++;
                    holder.edit.setVisibility(View.VISIBLE);
                    holder.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), Edit_post.class);
                            intent.putExtra("id",model.getId());
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();// Ensure the item count is correct
            }
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                adapter.notifyDataSetChanged();
                postCountTv.setText(String.valueOf(adapter.getItemCount()));


                // Notify adapter about data changes
            }
        };
      //  scrollView.post(new Runnable() {
      //      @Override
      //      public void run() {
      //          scrollView.scrollTo(0,-50);
      //      }
      //  });
    }
    public static class PostImageHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public   ImageView edit;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            edit = itemView.findViewById(R.id.edit);
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