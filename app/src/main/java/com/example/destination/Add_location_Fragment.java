package com.example.destination;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.Location.LocationForPost;
import com.example.destination.adapter.GalleryAdapter;
import com.example.destination.model.GalleryImages;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_location_Fragment extends Fragment {


    private EditText descET;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ImageButton backBtn, nextBtn,setLocation;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;
    private Uri imageUri;
    private FirebaseUser user;
    private Dialog dialog;
    public static int list_size = 0;
    public static LatLng finallatLang;
    //Gps chlnelu depqum dusa qcm


    private ActivityResultLauncher<PickVisualMediaRequest> launcher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri o) {
                            if (o == null) {
                                Toast.makeText(getContext(), "No image Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                imageUri = o;
                                imageView.setVisibility(View.VISIBLE);
                                nextBtn.setVisibility(View.VISIBLE);

                                Glide.with(getContext().getApplicationContext())
                                        .load(o)
                                        .into(imageView);

                                // Вызываем clickListener только после выбора изображения
                                clickListener();
                            }
                        }
                    });

    public Add_location_Fragment() {
    }

    @SuppressLint({"CutPasteId", "MissingInflatedId", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_location_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);
        recyclerView.setAdapter(adapter);



        setLocation.setOnClickListener(v -> {
            if(finallatLang == null) {
                Log.e("finallLatLang","null");
            }else {
                Log.e("finallLatLang","!null");
                Toast.makeText(getContext(), Double.toString(finallatLang.latitude), Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getActivity(), LocationForPost.class);
            startActivity(intent);

        });

        imageView.setOnClickListener(v -> launcher.launch(
                new PickVisualMediaRequest.Builder().setMediaType(
                        ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build()));
        nextBtn.setOnClickListener(v -> {
            Log.d("ButtonClick", "Next button clicked!");

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child("Post Images/"
                    + System.currentTimeMillis());
            dialog.show();
            storageReference.putFile(imageUri)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                                    uploadData(uri.toString()));
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getContext(),
                                    "Failed to upload post", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void init(View view) {
        descET = view.findViewById(R.id.descriptionET);
        imageView = view.findViewById(R.id.imageView);
        backBtn = view.findViewById(R.id.back_btn);
        nextBtn = view.findViewById(R.id.next_btn);
        recyclerView = view.findViewById(R.id.recyclerView);
        setLocation = view.findViewById(R.id.set_locaion);
        user = FirebaseAuth.getInstance().getCurrentUser();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.dialog_bg, null));
        dialog.setCancelable(false);
    }

    private void clickListener() {
        adapter.sendImage(pickUri -> {
            Glide.with(getContext())
                    .load(pickUri)
                    .into(imageView);

            imageUri = pickUri;
            imageView.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);


        });
    }

    public void uploadData(String imageURL) {
        //CollectionReference reference = FirebaseFirestore.getInstance().collection("users")
        //        .document(user.getUid()).collection("Post Images");
        CollectionReference reference = FirebaseFirestore.getInstance().collection("userPosts");


        List<String> list = new ArrayList<>();


        String description = descET.getText().toString();
        String id = reference.document().getId();


        Map<String, Object> map = new HashMap<>();
        map.put("description", description);
        map.put("imageUrl", imageURL);
        map.put("timestamp", FieldValue.serverTimestamp());
        map.put("id", id);
        map.put("Location", new GeoPoint(finallatLang.latitude,finallatLang.longitude));


        map.put("profileImage", String.valueOf(user.getPhotoUrl()));
        String username = user.getDisplayName();
        if (username == null || username.isEmpty()) {
            // Имя пользователя не установлено или пусто, выполните необходимые действия
            Log.e("NoUsername", "Username is null or empty");
        } else {
            // Имя пользователя доступно, используйте его для передачи в Firestore
            map.put("username", username);
        }
            map.put("likeCount", list);
        map.put("comments",list);
        map.put("uid", user.getUid());

        reference.document(id).set(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firestore", "Document uploaded successfully");
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Firestore", "Error uploading document: " + task.getException());
                        Toast.makeText(getContext(), "Error:" + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                });

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
//Ashxatuma
    public void onResume() {
        super.onResume();
                adapter.notifyDataSetChanged();

    }


}


// 40°50'21.0"N 44°27'31.9"E

// 40°47'52.2"N 44°31'25.2"E
