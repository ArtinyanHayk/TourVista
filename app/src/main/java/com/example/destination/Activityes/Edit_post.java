package com.example.destination.Activityes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.destination.Fragments.ProfileFragment;
import com.example.destination.Location.LocationForPost;
import com.example.destination.R;
import com.example.destination.adapter.GalleryAdapter;
import com.example.destination.model.GalleryImages;
import com.example.destination.model.HomeModel;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Edit_post extends AppCompatActivity {
    String id;
    private TextView nameTv;
    private EditText descET;
    private ImageView backBtn,profile_image;
    ImageSlider imageView;
    private RecyclerView recyclerView;
    private ImageButton setLocation;
    private Button nextBtn;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;
    private List<Uri> imageUris;
    private FirebaseUser user;
    private Dialog dialog,dialogDelete;
    public static int list_size = 0;
    public  static LatLng finallatLang;
    String username;
    String finalImageUrl;
    HomeModel model;
    ImageView delete;
    //Gps chlnelu depqum dusa qcm


    private ActivityResultLauncher<PickVisualMediaRequest> launcher =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(),
                    new ActivityResultCallback<List<Uri>>() {
                        @Override
                        public void onActivityResult(List<Uri> uris) {
                            if (uris == null || uris.isEmpty()) {
                                Toast.makeText(Edit_post.this, "No images Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                imageUris = uris;

                                // Вызываем clickListener только после выбора изображений
                                clickListener();
                            }
                        }
                    });
    private void clickListener() {
        adapter.sendImage(pickUri -> {
            imageUris = pickUri;
            ArrayList<SlideModel> imageList = new ArrayList<>();


            imageList.add(new SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct.", ScaleTypes.CENTER_CROP));
            imageList.add(new SlideModel("https://bit.ly/3fLJf72", "And people do that.",ScaleTypes.CENTER_CROP));
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageList(imageList);


        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        init();

        if(getIntent() != null && !getIntent().getExtras().isEmpty()){
            id = getIntent().getStringExtra("id");
            Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

            FirebaseFirestore.getInstance().collection("userPosts").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            model = task.getResult().toObject(HomeModel.class);
                            if (!model.getId().isEmpty()) {
                                list = new ArrayList<>();
                                adapter = new GalleryAdapter(list);

                                descET.setText(model.getDescription());
                                finallatLang = model.getLocation();


                                DisplayMetrics displayMetrics = Edit_post.this.getResources().getDisplayMetrics();
                                int screenWidth = displayMetrics.widthPixels;
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                BitmapFactory.decodeResource(Edit_post.this.getResources(), R.drawable.map_icon, options);
                                float aspectRatio = options.outWidth / (float) options.outHeight;
                                int calculatedHeight = (int) (screenWidth / aspectRatio);
                                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                                layoutParams.height = calculatedHeight;
                                imageView.setLayoutParams(layoutParams);
                                // recyclerView.setAdapter(adapter);
                                setLocation.setOnClickListener(v -> {
                                    if (finallatLang == null) {
                                        Log.e("finallLatLang", "null");
                                    } else {
                                        Log.e("finallLatLang", "!null");
                                        // Toast.makeText(Edit_post.this, Double.toString(finallatLang.latitude), Toast.LENGTH_SHORT).show();
                                    }
                                    if (isGPSEnabled()) {
                                        Intent intent = new Intent(Edit_post.this, LocationForPost.class);
                                        startActivity(intent);
                                    } else {
                                        showGPSEnableDialog();
                                    }

                                });
                                backBtn.setOnClickListener(v -> onBackPressed());

                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_post.this);
                                        builder.setMessage("Are you sure you want to delete this post?");
                                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                // Показать диалог удаления
                                                dialogDelete.show();
                                                // Удалить пост из Firestore
                                                FirebaseFirestore.getInstance().collection("userPosts").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        dialogDelete.dismiss();
                                                        onBackPressed();
                                                    }
                                                });
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss(); // Просто закрываем диалог
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.dialog_bg,null));
                                        dialog.show();
                                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

                                    }

                                });

                                imageView.setOnClickListener(v -> launcher.launch(
                                        new PickVisualMediaRequest.Builder().setMediaType(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build()));
                                nextBtn.setOnClickListener(v -> {
                                    if (imageUris == null ) {
                                        if(descET.getText() == null || descET.getText().length() < 3){
                                            return;
                                        }else {
                                            dialog.show();
                                            uploadData("");
                                        }
                                    } else if (imageUris.isEmpty()) {
                                        return;

                                    }else{
                                        Log.d("ButtonClick", "Next button clicked!");
                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageReference = storage.getReference().child("Post Images/"
                                                + System.currentTimeMillis());
                                        dialog.show();


                                        storageReference.putFile(imageUris.get(0))
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                                                                uploadData(uri.toString()));
                                                    } else {
                                                        dialog.dismiss();
                                                        Toast.makeText(Edit_post.this,
                                                                "Failed to upload post", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }


                                });
                                FirebaseFirestore.getInstance().collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            String  profileURL = (String) documentSnapshot.get("imageURL");
                                            String name = (String) documentSnapshot.get("userName");

                                            Glide.with(Edit_post.this.getApplicationContext())
                                                    .load(profileURL)
                                                    .placeholder(R.drawable.ic_person)
                                                    .timeout(6500)
                                                    .into(profile_image);

                                            nameTv.setText(name);

                                        }

                                    }
                                });


                            }
                        }else{
                            Toast.makeText(Edit_post.this, "null", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        
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
        nextBtn = findViewById(R.id.edit);
        nextBtn.setVisibility(View.VISIBLE);
        // recyclerView = findViewById(R.id.recyclerView);
        setLocation = findViewById(R.id.set_locaion);
        user = FirebaseAuth.getInstance().getCurrentUser();
        delete = findViewById(R.id.delete);
        dialog = new Dialog(Edit_post.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialogDelete = new Dialog(Edit_post.this);
        dialogDelete.setContentView(R.layout.delete_dialog);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDelete.setCancelable(false);
    }
    private  boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) Edit_post.this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showGPSEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_post.this);
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

    public void uploadData(String imageURL) {
        //CollectionReference reference = FirebaseFirestore.getInstance().collection("users")
        //        .document(user.getUid()).collection("Post Images");
        CollectionReference reference = FirebaseFirestore.getInstance().collection("userPosts");
        String finalImageUrl = imageURL;
        if(finalImageUrl  == null || finalImageUrl .isEmpty()){
            finalImageUrl  = model.getImageUrl();
        }

        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        ////////////////
        //      Toast.makeText(Edit_post.this, time, Toast.LENGTH_SHORT).show();

        user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference Currentuser = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());
        String finalImageUrl1 = finalImageUrl;
        Currentuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    username = (String) task.getResult().get("userName");
                    //Toast.makeText(Edit_post.this, username, Toast.LENGTH_SHORT).show();
                    List<String> list = new ArrayList<>();
                    Toast.makeText(Edit_post.this, "2", Toast.LENGTH_SHORT).show();
                    String description = descET.getText().toString();
                    Map<String, Object> map = new HashMap<>();
                    map.put("description", description);
                    map.put("imageUrl", finalImageUrl1);
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
                    reference.document(id).update(map)
                            .addOnCompleteListener(toFirebase -> {
                                if (toFirebase.isSuccessful()) {
                                    Log.d("Firestore", "Document uploaded successfully");
                                    nextBtn.setVisibility(View.GONE);
                                    descET.setText(null);
                                    finallatLang = null;
                                    Toast.makeText(Edit_post.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                } else {
                                  //  Log.e("Firestore", "Error uploading document: " + task.getException());
                                  //  Toast.makeText(Edit_post.this, "Error:" + task.getException().getMessage(),
                                  //          Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            });

                }
            }
        });


    }


}