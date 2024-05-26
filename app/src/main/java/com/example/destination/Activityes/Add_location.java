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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.icu.number.Scale;
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
import com.example.destination.Chat.Chat;
import com.example.destination.Location.LocationForPost;
import com.example.destination.R;
import com.example.destination.adapter.GalleryAdapter;
import com.example.destination.model.GalleryImages;
import com.example.destination.utils.BaseApplication;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Service;
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

import de.hdodenhof.circleimageview.CircleImageView;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Add_location  extends BaseApplication  {
    private TextView nameTv;
    private EditText descET;
    private ImageView backBtn;
    CircleImageView profile_image;
    ImageSlider imageView;
    private RecyclerView recyclerView;
    private ImageButton setLocation;
    private Button nextBtn;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;
    private List<Uri> imageUris;


    private FirebaseUser user;
    private Dialog dialog;
    public static int list_size = 0;
    public  static LatLng finallatLang;
    String username;

    //Gps chlnelu depqum dusa qcm


    private ActivityResultLauncher<PickVisualMediaRequest> launcher =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(),
                    new ActivityResultCallback<List<Uri>>() {
                        @Override
                        public void onActivityResult(List<Uri> uris) {
                            if (uris == null || uris.isEmpty()) {
                                Toast.makeText(Add_location.this, "No images Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                imageUris = uris;
                                nextBtn.setVisibility(View.VISIBLE);

                                MotionToast.Companion.createColorToast(
                                        (Activity) Add_location.this,
                                        "Info!",
                                        "You can only select up only 1 picture",
                                        MotionToastStyle.INFO,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(Add_location.this, www.sanju.motiontoast.R.font.helveticabold)
                                );

                                // Вызываем clickListener только после выбора изображений
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

        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);

        DisplayMetrics displayMetrics = Add_location.this.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(Add_location.this.getResources(), R.drawable.map_icon, options);
        float aspectRatio = options.outWidth / (float) options.outHeight;
        int calculatedHeight = (int) (screenWidth / aspectRatio);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = calculatedHeight;
        imageView.setLayoutParams(layoutParams);
       // recyclerView.setAdapter(adapter);
        setLocation.setOnClickListener(v -> {
            if(finallatLang == null) {
                Log.e("finallLatLang","null");
            }else {
                Log.e("finallLatLang","!null");
               // Toast.makeText(Add_location.this, Double.toString(finallatLang.latitude), Toast.LENGTH_SHORT).show();
            }
            if (isGPSEnabled()) {
                Intent intent = new Intent(Add_location.this, LocationForPost.class);
                startActivity(intent);
            }else{
                showGPSEnableDialog();
            }

        });
        backBtn.setOnClickListener(v -> onBackPressed());

        imageView.setOnClickListener(v -> {
            launcher.launch(
                    new PickVisualMediaRequest.Builder().setMediaType(
                            ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());


        });

        nextBtn.setOnClickListener(v -> {

            Log.d("ButtonClick", "Next button clicked!");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child("Post Images/"
                    + System.currentTimeMillis());
            dialog.show();


                storageReference.putFile(imageUris.get(0))
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
        nextBtn.setVisibility(View.GONE);
       // recyclerView = findViewById(R.id.recyclerView);
        setLocation = findViewById(R.id.set_locaion);
        user = FirebaseAuth.getInstance().getCurrentUser();

        dialog = new Dialog(Add_location.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
            imageUris = pickUri;
            ArrayList<SlideModel> imageList = new ArrayList<>();


            imageList.add(new SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct.",ScaleTypes.CENTER_CROP));
            imageList.add(new SlideModel("https://bit.ly/3fLJf72", "And people do that.",ScaleTypes.CENTER_CROP));
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageList(imageList);


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
  //      Toast.makeText(Add_location.this, time, Toast.LENGTH_SHORT).show();

        user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference Currentuser = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());
        Currentuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                   username = (String) task.getResult().get("userName");
                    //Toast.makeText(Add_location.this, username, Toast.LENGTH_SHORT).show();
                    List<String> list = new ArrayList<>();


                    String description = descET.getText().toString();
                    String id = reference.document().getId();



                    Map<String, Object> map = new HashMap<>();
                    map.put("description", description);
                    map.put("imageUrl", imageURL);
                    map.put("timestamp", com.google.firebase.Timestamp.now());
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
                                    nextBtn.setVisibility(View.GONE);
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
