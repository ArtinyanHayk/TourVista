package com.example.destination.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


}

