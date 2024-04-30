package com.example.destination.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.destination.R;
import com.example.destination.adapter.SendLayoutAdapter;
import com.example.destination.databinding.ShareBottomSheetLayoutBinding;
import com.example.destination.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;




import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.destination.R;
import com.example.destination.adapter.SendLayoutAdapter;
import com.example.destination.databinding.ShareUserItemBinding;
import com.example.destination.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Share_bottom_sheet_layout extends BottomSheetDialogFragment {



        protected ShareBottomSheetLayoutBinding binding;
        private List<String> commentLikes;
        private FirebaseUser user;
        private RecyclerView recyclerView;
        private List<UserModel> list;
        private SendLayoutAdapter adapter;
        private String id,username;
        private String profileImage;
        List<String> followings;






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

        // Получаем список подписок пользователя из Firestore
        DocumentReference currentUserRef = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
        currentUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        followings = (List<String>) document.get("following");

                        // Инициализируем список, RecyclerView, адаптер и загружаем данные
                        list = new ArrayList<>();
                        recyclerView = view.findViewById(R.id.recyclerView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        adapter = new SendLayoutAdapter(getContext(), list);
                        recyclerView.setAdapter(adapter);
                        loadDataFromFirestore();
                    }
                } else {
                    // Обрабатываем ошибку
                }
            }
        });
    }


        @SuppressLint("NotifyDataSetChanged")
        private void loadDataFromFirestore() {
            for(String id: followings){
                FirebaseFirestore.getInstance().collection("users").document(id)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    UserModel model = task.getResult().toObject(UserModel.class);
                                    list.add(model);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
            }


        }



    }

