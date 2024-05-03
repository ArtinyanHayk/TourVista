package com.example.destination.Chat;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.Location.LocationForUser;
import com.example.destination.R;
import com.example.destination.adapter.GalleryAdapter;
import com.example.destination.adapter.MessagesAdapter;
import com.example.destination.model.ChatModel;
import com.example.destination.model.GalleryImages;
import com.example.destination.model.MessageModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.BaseApplication;
import com.example.destination.utils.FirbaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends BaseApplication {

    EditText messageEditText;
    String chatId;
    FirebaseUser user;
    String id2,profilePicUrl;
    ChatModel chatModel;
    MessagesAdapter adapter;
    ImageView sendBtn;
    TextView name, onlineTv;
    ImageView bacBtn,addBtn;
    CircleImageView profilePic;
    RecyclerView recyclerView;
    Dialog dialog;
    List<String> urls = new ArrayList<>();
    boolean online;
    private GalleryAdapter galleryAdapter;
    private List<GalleryImages> list;
    private List<Uri> imageUris;
    private ActivityResultLauncher<PickVisualMediaRequest> launcher =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(),
                    new ActivityResultCallback<List<Uri>>() {
                        @Override
                        public void onActivityResult(List<Uri> uris) {

                            if (uris == null || uris.isEmpty()) {
                                Toast.makeText(Chat.this, "No images Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                imageUris = new ArrayList<>();

                                imageUris = uris;

                                // Вызываем clickListener только после выбора изображений
                                clickListener();
                            }
                        }
                    });


                            private void clickListener() {
                                galleryAdapter.sendImage(pickUri -> {
                                    imageUris = pickUri;
                                });
                            }




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bacBtn = findViewById(R.id.back_btn);
        name = findViewById(R.id.name);
        messageEditText = findViewById(R.id.messageEditTxt);
        profilePic = findViewById(R.id.profilePic);
        sendBtn = findViewById(R.id.sendBtn);
        recyclerView = findViewById(R.id.recyclerView);
        onlineTv = findViewById(R.id.online);
        addBtn = findViewById(R.id.addToMessage);

        list = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(list);

        dialog = new Dialog(Chat.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        bacBtn.setOnClickListener(v -> finish());

        addBtn.setOnClickListener(v ->launcher.launch(new PickVisualMediaRequest.Builder().setMediaType(
                ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build()) );

        id2 = getIntent().getStringExtra("person2_id");
        profilePicUrl = getIntent().getStringExtra("profilePic");


        FirebaseFirestore.getInstance().collection("users").document(id2).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists() && error == null) {
                    UserModel model = value.toObject(UserModel.class);

                    if (model.getonline()) {
                        onlineTv.setText("online");
                        onlineTv.setTextColor(getResources().getColor(R.color.light_green));
                    } else {
                        onlineTv.setText("offline");
                        onlineTv.setTextColor(getResources().getColor(R.color.light_gray));
                    }
                    name.setText(model.getUserName());


                }
            }
        });
        Glide.with(Chat.this)
                .load(profilePicUrl)
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(profilePic);
        chatId = FirbaseUtil.chatId(user.getUid(), id2);
        getOrCreateChatroomModel();
        setupChatRecyclerView();


        adapter.OnPressed(new MessagesAdapter.OnPressed() {
            @Override
            public void onGetLocation(GeoPoint location) {
                if(AndroidUtil.isGPSEnabled(Chat.this)) {
                    Intent intent = new Intent(Chat.this, LocationForUser.class);
                    intent.putExtra("Location", new LatLng(location.getLatitude(),location.getLongitude()));
                    startActivity(intent);
                }
                else{
                    AndroidUtil.showGPSEnableDialog(Chat.this);
                }
            }
        });


        sendBtn.setOnClickListener(v -> {
            String gettxtMessage = messageEditText.getText().toString();
            if (gettxtMessage.isEmpty() && imageUris == null) {
                return;

            }
            else {
                try {
                    if(imageUris != null && !imageUris.isEmpty() ){
                        SendMessage(gettxtMessage, user.getUid());

                    }else{
                        message(gettxtMessage, user.getUid());
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        });


    }

    void setupChatRecyclerView() {
        Query query = FirbaseUtil.getChatMessageReference(chatId)
                .orderBy("time", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class).build();

        adapter = new MessagesAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void SendMessage(String message, String id) throws IOException {
        Timestamp timestamp = Timestamp.now();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("Chats Images/");

        dialog.show();
        urls = new ArrayList<>();
        List<UploadTask> uploadTasks = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
            uploadTasks.add(imageRef.putFile(imageUri));
        }
        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                if (task.isSuccessful()) {
                    for (UploadTask uploadTask : uploadTasks) {
                        StorageReference storageReference = (StorageReference) uploadTask.getResult().getStorage();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urls.add(uri.toString());
                                if (urls.size() == imageUris.size()) {
                                    message(message, id);
                                }
                            }
                        });
                    }
                } else {
                    // Handle error
                }
            }

        });
    }

    void message(String message,String id) {
        MessageModel messageModel = new MessageModel(message, id, com.google.firebase.Timestamp.now(), urls);
        chatModel.setLastMessage(message);
        chatModel.setLastMessageTime(com.google.firebase.Timestamp.now());
        chatModel.setLastMsgSenderId(user.getUid());
        if(urls == null ||  urls.isEmpty()){
            FirbaseUtil.getChatReference(chatId).set(chatModel).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(this, "Check your connection", Toast.LENGTH_SHORT).show();
                } else {
                }
            });

        }
        else {
            FirbaseUtil.getChatReference(chatId).set(chatModel).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(this, "Check your connection", Toast.LENGTH_SHORT).show();
                } else {
                    imageUris = null;
                    urls = null;
                    dialog.dismiss();

                }
            });
        }

        FirbaseUtil.getChatMessageReference(chatId).add(messageModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageEditText.setText("");
            }
        });
    }

    void getOrCreateChatroomModel() {
        FirbaseUtil.getChatReference(chatId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatModel = task.getResult().toObject(ChatModel.class);
                if (chatModel == null) {
                    // First time chat
                    chatModel = new ChatModel(
                            "",
                            com.google.firebase.Timestamp.now(),
                            "",
                            Arrays.asList(FirbaseUtil.currentUsersId(), id2)


                    );
                    FirbaseUtil.getChatReference(chatId).set(chatModel);
                }
            }
        });
    }



}
