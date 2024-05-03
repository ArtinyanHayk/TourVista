package com.example.destination.Fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.service.credentials.Action;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.destination.R;
import com.example.destination.adapter.SendLayoutAdapter;
import com.example.destination.databinding.ShareBottomSheetLayoutBinding;
import com.example.destination.model.ChatModel;
import com.example.destination.model.MessageModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.FirbaseUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Share_bottom_sheet_layout extends BottomSheetDialogFragment {


    protected ShareBottomSheetLayoutBinding binding;
    private List<String> commentLikes;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private List<UserModel> list;
    private SendLayoutAdapter adapter;
    private String id, username;

    List<String> followings;
    Double latitude;
    Double longitude;
    String postImage;
    String profilePic;
    String description;
    String postUsername;
    String posterid;
    CollectionReference chats;
    ImageView viber,whatsapp,messenger,telegram;


    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ShareBottomSheetLayoutBinding.inflate(getLayoutInflater());
        user = FirebaseAuth.getInstance().getCurrentUser();


        binding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
                layoutParams.height = getResources().getDisplayMetrics().heightPixels; // Установите желаемую высоту или используйте другой размер
                binding.getRoot().setLayoutParams(layoutParams);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt("position");
            posterid = args.getString("uid");
                latitude = args.getDouble("latitude");
                longitude = args.getDouble("longitude");
            postImage = args.getString("postImage");
            profilePic = args.getString("profilePic");
            description = args.getString("Description");
            postUsername = args.getString("PostUsername");
        }


        // Получаем список подписок пользователя из Firestore
        DocumentReference currentUserRef = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
        currentUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModel model =document.toObject(UserModel.class);
                        followings = (List<String>) document.get("following");

                        // Инициализируем список, RecyclerView, адаптер и загружаем данные
                        list = new ArrayList<>();
                        recyclerView = view.findViewById(R.id.recyclerView);
                        viber = view.findViewById(R.id.viber);
                        whatsapp = view.findViewById(R.id.whatsapp);
                        messenger = view.findViewById(R.id.messenger);
                        telegram = view.findViewById(R.id.telegram);
                        //
                        viber.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent toViber = new Intent(Intent.ACTION_SEND);
                                    toViber.putExtra(Intent.EXTRA_TEXT, postImage + "\n\n\n\n\n" + description); // Add your text here
                                    // Add your image here
                                    // Set the MIME type for text
                                    toViber.setType("text/plain"); // Set the MIME type for image
                                    toViber.setPackage("com.viber.voip"); // Specify the Viber package name
                                    startActivity(toViber);
                            }catch (ActivityNotFoundException e){
                                Toast.makeText(getActivity(), "Viber is not installed", Toast.LENGTH_SHORT).show();
                            }

                            }
                        });
                        telegram.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                     Intent toTelegram = new Intent(Intent.ACTION_SEND);
                                    toTelegram.putExtra(Intent.EXTRA_TEXT, postImage + "\n\n\n\n\n" + description); // Add your text here
                                    // Add your image here
                                    // Set the MIME type for text
                                    toTelegram.setType("text/plain"); // Set the MIME type for image
                                    toTelegram.setPackage("org.telegram.messenger"); // Specify the Viber package name
                                    startActivity(toTelegram);
                            }catch (ActivityNotFoundException e){
                                Toast.makeText(getActivity(), "Telegram is not installed", Toast.LENGTH_SHORT).show();
                            }

                                 }



                        });
                        messenger.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    Intent toTelegram = new Intent(Intent.ACTION_SEND);
                                    toTelegram.putExtra(Intent.EXTRA_TEXT, postImage + "\n\n\n\n\n" + description); // Add your text here
                                    // Add your image here
                                    // Set the MIME type for text
                                    toTelegram.setType("text/plain"); // Set the MIME type for image
                                    toTelegram.setPackage("com.facebook.orca"); // Specify the Viber package name
                                    startActivity(toTelegram);

                                }catch (ActivityNotFoundException e){
                                    Toast.makeText(getActivity(), "Messenger is not installed", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        whatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent toTelegram = new Intent(Intent.ACTION_SEND);
                                    toTelegram.putExtra(Intent.EXTRA_TEXT, postImage + "\n\n\n\n\n" + description); // Add your text here
                                    // Add your image here
                                    // Set the MIME type for text
                                    toTelegram.setType("text/plain"); // Set the MIME type for image
                                    toTelegram.setPackage("com.whatsapp"); // Specify the Viber package name
                                    startActivity(toTelegram);
                            }catch (ActivityNotFoundException e){
                                Toast.makeText(getActivity(), "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
                            }


                            }
                        });



                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        adapter = new SendLayoutAdapter(getContext(),list);
                        recyclerView.setAdapter(adapter);
                        loadDataFromFirestore();
                        adapter.onPressed(new SendLayoutAdapter.onPressed() {

                            @Override
                            public void onSelect(String id) {
                                String chatId = FirbaseUtil.chatId(FirbaseUtil.currentUsersId(), id);
                                List<String> postImages = new ArrayList<>();
                                postImages.add(postImage);
                                MessageModel messageModel = new MessageModel();
                                if(latitude != 0 || longitude != 0){
                                    messageModel = new MessageModel(description, FirbaseUtil.currentUsersId(), posterid, postImages, Timestamp.now(), profilePic, postUsername, new GeoPoint(latitude, longitude));
                                }else{
                                     messageModel = new MessageModel(description, FirbaseUtil.currentUsersId(), posterid, postImages, Timestamp.now(), profilePic, postUsername);

                                }
                                FirbaseUtil.getChatReference(chatId).collection("messages").add(messageModel).addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(getContext(), "message", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }


                } else {
                    // Обрабатываем ошибку
                }
            }

        });

    }


    @SuppressLint("NotifyDataSetChanged")
    private void loadDataFromFirestore() {
        for (String id : followings) {
            FirebaseFirestore.getInstance().collection("users").document(id)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                UserModel model = task.getResult().toObject(UserModel.class);
                                list.add(model);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }


    }
    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}

