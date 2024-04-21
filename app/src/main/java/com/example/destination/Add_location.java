package com.example.destination;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.destination.Location.LocationForPost;
import com.example.destination.adapter.GalleryAdapter;
import com.example.destination.model.GalleryImages;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Add_location extends AppCompatActivity {
    private TextView nameTv;
    private EditText descET;
    private ImageView backBtn,imageView,profile_image;
    private RecyclerView recyclerView;
    private ImageButton setLocation;
    private Button nextBtn;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;
    private Uri imageUri;


    private FirebaseUser user;
    private Dialog dialog;
    public static int list_size = 0;
    public  static LatLng finallatLang;
    String username;

    //Gps chlnelu depqum dusa qcm


    private ActivityResultLauncher<PickVisualMediaRequest> launcher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri o) {
                            if (o == null) {
                                Toast.makeText(Add_location.this, "No image Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                imageUri = o;
                                imageView.setVisibility(View.VISIBLE);
                                nextBtn.setVisibility(View.VISIBLE);

                                Glide.with(Add_location.this.getApplicationContext())
                                        .load(o)
                                        .into(imageView);

                                // Вызываем clickListener только после выбора изображения
                                clickListener();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        init();
       // recyclerView.setHasFixedSize(true);
        nextBtn.setVisibility(View.INVISIBLE);
        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);
       // recyclerView.setAdapter(adapter);
        setLocation.setOnClickListener(v -> {
            if(finallatLang == null) {
                Log.e("finallLatLang","null");
            }else {
                Log.e("finallLatLang","!null");
                Toast.makeText(Add_location.this, Double.toString(finallatLang.latitude), Toast.LENGTH_SHORT).show();
            }
            if (isGPSEnabled()) {
                Intent intent = new Intent(Add_location.this, LocationForPost.class);
                startActivity(intent);
            }else{
                showGPSEnableDialog();
            }

        });
        backBtn.setOnClickListener(v -> onBackPressed());

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
                            Toast.makeText(Add_location.this,
                                    "Failed to upload post", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                   String  profileURL = (String) documentSnapshot.get("imageURL");
                   String name = (String) documentSnapshot.get("userName");

                    Glide.with(Add_location.this.getApplicationContext())
                            .load(profileURL)
                            .placeholder(R.drawable.ic_person)
                            .timeout(6500)
                            .into(profile_image);

                    nameTv.setText(name);

                }

            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void init() {
        descET = findViewById(R.id.descriptionET);
        //////////
        imageView = findViewById(R.id.post_image);
        nameTv = findViewById(R.id.username);
        profile_image = findViewById(R.id.image_profile);
        /////////
        backBtn = findViewById(R.id.back_btn);
        nextBtn = findViewById(R.id.share_btn);
       // recyclerView = findViewById(R.id.recyclerView);
        setLocation = findViewById(R.id.set_locaion);
        user = FirebaseAuth.getInstance().getCurrentUser();

        dialog = new Dialog(Add_location.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.dialog_bg, null));
        dialog.setCancelable(false);
    }

    private  boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) Add_location.this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showGPSEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_location.this);
        builder.setMessage("GPS is disabled. Do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.dialog_bg,null));
        alert.show();
    }

    private void clickListener() {
        adapter.sendImage(pickUri -> {
            Glide.with(Add_location.this)
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

        Date date = new Date();

// Преобразование объекта Date в Timestamp
        Timestamp timestamp = new Timestamp(date);

// Преобразование Timestamp в строку
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(date);

        ////////////////
        Toast.makeText(Add_location.this, time, Toast.LENGTH_SHORT).show();

        user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference Currentuser = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());
        Currentuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                   username = (String) task.getResult().get("userName");
                    Toast.makeText(Add_location.this, username, Toast.LENGTH_SHORT).show();
                    List<String> list = new ArrayList<>();


                    String description = descET.getText().toString();
                    String id = reference.document().getId();



                    Map<String, Object> map = new HashMap<>();
                    map.put("description", description);
                    map.put("imageUrl", imageURL);
                    map.put("timestamp", time);
                    map.put("id", id);
                    if(finallatLang != null) {
                        map.put("Location", new GeoPoint(finallatLang.latitude, finallatLang.longitude));
                    }


                    map.put("profileImage", String.valueOf(user.getPhotoUrl()));

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
                            .addOnCompleteListener(toFirebase -> {
                                if (toFirebase.isSuccessful()) {
                                    Log.d("Firestore", "Document uploaded successfully");
                                    imageView.setImageURI(null);
                                    nextBtn.setVisibility(View.GONE);
                                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.add_icon_borderless));
                                    descET.setText(null);
                                    finallatLang = null;
                                    Toast.makeText(Add_location.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                } else {
                                    Log.e("Firestore", "Error uploading document: " + task.getException());
                                    Toast.makeText(Add_location.this, "Error:" + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            });

                }
            }
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
