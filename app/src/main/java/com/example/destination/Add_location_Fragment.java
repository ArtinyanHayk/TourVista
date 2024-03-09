package com.example.destination;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.adapter.GalleryAdapter;
import com.example.destination.model.GalleryImages;
import com.github.dhaval2404.imagepicker.provider.CropProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_location_Fragment extends Fragment {

    EditText descET;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ImageButton backBtn, nextBtn;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;
    Uri imageUri;
    private FirebaseUser user;
    /////
   ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
       @Override
       public void onActivityResult(Uri o) {
           if(o == null){

               Toast.makeText(getContext(), "No image Selected", Toast.LENGTH_SHORT).show();
           }else{
               ///
               Uri image = o;
               ///

               Glide.with(getContext().getApplicationContext())
                       .load(o)
                       .into(imageView);

           }
       }
   });
   /////

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
        ///////////////
      imageView.setOnClickListener(v -> launcher.launch(new PickVisualMediaRequest.Builder().setMediaType
              (ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build()));
        //////////////



        clickListener();

    }

    private void clickListener() {
        adapter.sendImage(pickUri -> {
            imageUri = pickUri;
            Glide.with(getContext())
                    .load(pickUri)
                    .into(imageView);

            imageView.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);

           // CropImage.activity(pickUri)
           //         .setGuidelines(CropImageView.GuideLines.ON)
           //         .setAspectRatio(4,3)
           //         .start(getContext(),Add.this);


        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference().child("/Post Images"
                        + System.currentTimeMillis());
                storageReference.putFile(imageUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            uploadData(uri.toString());

                                        }
                                    });

                                }
                            }
                        });
            }
        });
    }

    public void uploadData(String imageURL) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid()).collection("Post Images");

        String description = descET.getText().toString();
        String id = reference.document().getId();


        Map<String, Object> map = new HashMap<>();
        map.put("description", description);
        map.put("imageUrl", imageURL);
        map.put("timestamp", FieldValue.serverTimestamp());
        map.put("id", id);

        map.put("profileImage", String.valueOf(user.getPhotoUrl()));
        map.put("username", user.getDisplayName() );
        map.put("likeCount", 0);
        //getLocation

        reference.document(id).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println();
                        } else {
                            Toast.makeText(getContext(), "Error:" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    private void init(View view) {
        descET = view.findViewById(R.id.descriptionET);
        imageView = view.findViewById(R.id.imageView);
        backBtn = view.findViewById(R.id.back_btn);
        nextBtn = view.findViewById(R.id.next_btn);
        recyclerView = view.findViewById(R.id.recyclerView);


        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dexter.withContext(getContext())
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/Dowload");
                                    if (file.exists()) {

                                        File[] files = file.listFiles();

                                        for (File file1 : files) {
                                            if (file1.getAbsolutePath().endsWith(".jpg") || file1.getAbsolutePath().endsWith(".png")) {
                                                list.add(new GalleryImages(Uri.fromFile(file1)));
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                            }
                        }).check();


            }
        });
    }

}


