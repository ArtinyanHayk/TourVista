package com.example.destination.Fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.destination.Activityes.Chat_serach;
import com.example.destination.R;
import com.example.destination.adapter.SearchUserReciclerAdapter;
import com.example.destination.adapter.SendLayoutAdapter;
import com.example.destination.databinding.ShareBottomSheetLayoutBinding;
import com.example.destination.model.MessageModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.FirbaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Share_bottom_sheet_layout extends BottomSheetDialogFragment {


    protected ShareBottomSheetLayoutBinding binding;
    private List<String> commentLikes;
    private FirebaseUser user;
    private RecyclerView recyclerView,searchRecyclerView;
    private List<UserModel> list;
    private SendLayoutAdapter adapter;
    private SearchUserReciclerAdapter searchAdapter;
    private String id, username;

    List<String> followings;
    List<UserModel> filteredList;
    Double latitude;
    Double longitude;
    String postImage;
    String profilePic;
    String description;
    String postUsername;
    String posterid;
    CollectionReference chats,reference;
    ImageView viber, whatsapp, messenger, telegram;
    SearchView searchView;
    CollectionReference currentUserRef;





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
        currentUserRef = FirebaseFirestore.getInstance().collection("users");
        currentUserRef.document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModel model = document.toObject(UserModel.class);
                        followings = (List<String>) document.get("following");
                        init(view);
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
                                } catch (ActivityNotFoundException e) {
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
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(getActivity(), "Telegram is not installed", Toast.LENGTH_SHORT).show();
                                }

                            }


                        });
                        messenger.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent toTelegram = new Intent(Intent.ACTION_SEND);
                                    toTelegram.putExtra(Intent.EXTRA_TEXT, postImage + "\n\n\n\n\n" + description); // Add your text here
                                    // Add your image here
                                    // Set the MIME type for text
                                    toTelegram.setType("text/plain"); // Set the MIME type for image
                                    toTelegram.setPackage("com.facebook.orca"); // Specify the Viber package name
                                    startActivity(toTelegram);

                                } catch (ActivityNotFoundException e) {
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
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(getActivity(), "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        adapter = new SendLayoutAdapter(getContext(), list);
                        recyclerView.setAdapter(adapter);
                        loadDataFromFirestore();
                        adapter.onPressed(new SendLayoutAdapter.onPressed() {

                            @Override
                            public void onSelect(String id) {
                                String chatId = FirbaseUtil.chatId(FirbaseUtil.currentUsersId(), id);
                                List<String> postImages = new ArrayList<>();
                                postImages.add(postImage);
                                String MessageId =  FirbaseUtil.getChatReference(chatId).collection("messages").document().getId();
                                MessageModel messageModel = new MessageModel();
                                if (latitude != 0 || longitude != 0) {
                                    messageModel = new MessageModel(description, FirbaseUtil.currentUsersId(), posterid, postImages, Timestamp.now(), profilePic, postUsername, new GeoPoint(latitude, longitude),MessageId);
                                } else {
                                    messageModel = new MessageModel(description, FirbaseUtil.currentUsersId(), posterid, postImages, Timestamp.now(), profilePic, postUsername,MessageId);

                                }
                                FirbaseUtil.getChatReference(chatId).collection("messages").document(MessageId).set(messageModel).addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }
                                });
                            }
                        });
                        searchAdapter.onPressed(new SearchUserReciclerAdapter.onPressed() {
                            @Override
                            public void onPress(String id) {
                                String chatId = FirbaseUtil.chatId(FirbaseUtil.currentUsersId(), id);
                                List<String> postImages = new ArrayList<>();
                                postImages.add(postImage);
                                String MessageId =  FirbaseUtil.getChatReference(chatId).collection("messages").document().getId();
                                MessageModel messageModel = new MessageModel();
                                if (latitude != 0 || longitude != 0) {
                                    messageModel = new MessageModel(description, FirbaseUtil.currentUsersId(), posterid, postImages, Timestamp.now(), profilePic, postUsername, new GeoPoint(latitude, longitude),MessageId);
                                } else {
                                    messageModel = new MessageModel(description, FirbaseUtil.currentUsersId(), posterid, postImages, Timestamp.now(), profilePic, postUsername,MessageId);

                                }
                                FirbaseUtil.getChatReference(chatId).collection("messages").document(MessageId).set(messageModel).addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();
                                    } else {

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
    void init(View view){
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        viber = view.findViewById(R.id.viber);
        whatsapp = view.findViewById(R.id.whatsapp);
        messenger = view.findViewById(R.id.messenger);
        telegram = view.findViewById(R.id.telegram);
        searchView = view.findViewById(R.id.searchView);
        searchRecyclerView = view.findViewById(R.id.search_recycler_view);
        filteredList = new ArrayList<>();
        searchView.clearFocus();
        searchView.requestFocus();
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        searchAdapter = new SearchUserReciclerAdapter(filteredList, getActivity());
        searchRecyclerView.setAdapter(searchAdapter);


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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    searchRecyclerView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    return false;
                }
                else {
                    filterList(newText);
                    return true;
                }
            }
        });





    }
    private void filterList(String text) {
        filteredList.clear();

        currentUserRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("search", "error: " + error);
                    return;
                }
                if (value == null || value.isEmpty()) {
                    return;
                }

                for (QueryDocumentSnapshot snapshot : value) {
                    if (!snapshot.exists()) {
                        continue;
                    }
                    UserModel model = snapshot.toObject(UserModel.class);

                    if (model.getUserName().toLowerCase().contains(text.toLowerCase()) && followings.contains(model.getUserId())) {

                        filteredList.add(new UserModel(
                                null,
                                model.getUserName(),
                                null,
                                model.getUserId(),
                                new ArrayList<>(),
                                new ArrayList<>(),
                                model.getImageURL(),
                                model.getonline()

                        ));
                    }
                }
                if (filteredList.isEmpty()) {
                    searchRecyclerView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    searchRecyclerView.setVisibility(View.VISIBLE);
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });
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

