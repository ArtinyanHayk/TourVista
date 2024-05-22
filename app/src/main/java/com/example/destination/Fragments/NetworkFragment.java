package com.example.destination.Fragments;




import static androidx.appcompat.content.res.AppCompatResources.getDrawable;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.destination.Location.LocationForUser;
import com.example.destination.R;
import com.example.destination.adapter.HomeAdapter;
import com.example.destination.databinding.ActivityMainBinding;
import com.example.destination.model.HomeModel;
import com.example.destination.Activityes.search_Activity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkFragment extends Fragment {
    private RecyclerView recyclerView;
    HomeAdapter adapter;
    private List<HomeModel> list;
    private FirebaseUser user;
    List<String> list222;

    ImageButton search_user;
    Date curent_date;


    private ActivityMainBinding binding;


    public NetworkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.network_fragment, container, false);
        //return inflater.inflate(R.layout.network_fragment,binding.getRoot());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        curent_date = new Date();

       list222 = new ArrayList<>();

       binding = ActivityMainBinding.inflate(getLayoutInflater());






        list = new ArrayList<>();
        adapter = new HomeAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();
        search_user.setOnClickListener(v -> {
            Intent intent2 = new Intent(getActivity(), search_Activity.class);
            startActivity(intent2);

        });


        adapter.OnPressed(new HomeAdapter.OnPressed() {

            @Override
            public void onLiked(int position, String id, String uid, List<String> likeList) {
                DocumentReference reference = FirebaseFirestore.getInstance().collection("userPosts").document(id);

                if (likeList == null) {
                    likeList = new ArrayList<>();
                }

                if (likeList.contains(user.getUid()) ) {
                    Log.e("unlike","-");
                    likeList.remove(user.getUid()); // Unlike
                } else  {
                    likeList.add(user.getUid()); // Like
                }

                Map<String, Object> map = new HashMap<>();
                map.put("likeCount", likeList);
                reference.update(map)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            loadDataFromFirestore();
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error updating document", e);
                            Toast.makeText(getContext(), "Error updating document", Toast.LENGTH_SHORT).show();
                        });



            }

            @Override
            public void onComment(int position, String id, String uid) {
                //showDialog();

                if(getActivity() != null) {
                    Commets_BottomSheet fragment = new Commets_BottomSheet();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putString("id", id);
                    args.putString("uid", uid);
                    fragment.setArguments(args);
                    fragment.show(getActivity().getSupportFragmentManager(), "comment bottom sheet dialog");
                }



            }


            @Override
            public void onGetLocation(int position, String id, String uid, LatLng location) {
                if(isGPSEnabled()) {
                    Intent intent = new Intent(getActivity(), LocationForUser.class);
                    intent.putExtra("Location", location);
                    startActivity(intent);
                }
                else{
                    showGPSEnableDialog();
                }


            }

            @Override
            public void onSharePost(int position, String id, String uid, String postImageUrl, String profileImage, String username, String Description, LatLng location) {
                Share_bottom_sheet_layout fragment = new Share_bottom_sheet_layout();
                Bundle args = new Bundle();
                args.putInt("position", position);
                args.putString("id", id);
                args.putString("uid", uid);
                if(location != null){
                    args.putDouble("latitude",location.latitude);
                    args.putDouble("longitude",location.longitude);
                }
                else{
                    args.putDouble("latitude",0);
                    args.putDouble("longitude",0);
                }
                   args.putString("postImage",postImageUrl);
                  args.putString("profilePic",profileImage);
                 args.putString("Description",Description);
                args.putString("PostUsername",username);
                fragment.setArguments(args);

                fragment.show(getActivity().getSupportFragmentManager(), "comment bottom sheet dialog");

            }


        });
    }



    private boolean isGPSEnabled() {
        Context context = getContext();
        if (context != null) {
            try {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                Log.e("isGpsEnabled/NetworkFragment", "Ошибка при проверке GPS: " + e.getMessage());
                return false;
            }
        } else {
            Log.e("isGpsEnabled/NetworkFragment", "Контекст равен null");
            return false;
        }
    }
    private void showGPSEnableDialog() {
        Context context = getContext();
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Для просмотра местоположения включите GPS")
                    .setCancelable(false)
                    .setPositiveButton("Включить GPS", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());

            AlertDialog alert = builder.create();
            Window window = alert.getWindow();
            alert.show();
            if (window != null) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                window.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_bg));
            }

        } else {
            Log.e("showGPSEnableDialog", "Контекст равен null");
        }
    }


    private void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_user = view.findViewById(R.id.search_user_btn);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();





    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDataFromFirestore() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("userPosts");






        reference.addSnapshotListener((value, error) -> {
            Log.e("reference updated", "reference");
            if (error != null) {
                Log.e("Error:", error.getMessage());
                return;
            }
            if (value == null || value.isEmpty()) {
                return; // Нет данных в снимке, просто выходим
            }

            // Очистить список перед добавлением новых данных
            list.clear();



            for (QueryDocumentSnapshot snapshot : value) {
                if (!snapshot.exists()) {
                    continue;
                }


                HomeModel model = snapshot.toObject(HomeModel.class);
                GeoPoint geoPoint = (GeoPoint) snapshot.get("Location");




                Object likeCountObject = snapshot.get("likeCount");
                List<String> likeCountList = likeCountObject instanceof List ? (List<String>) likeCountObject : null;
                if (geoPoint == null) {
                    list.add(new HomeModel(
                            model.getProfileImage(),
                            model.getImageUrl(),
                            model.getUid(),
                            model.getDescription(),
                            model.getId(),
                            model.getUsername(),
                            snapshot.getTimestamp("timestamp"),
                            likeCountList
                    ));
                } else {
                    list.add(new HomeModel(
                            model.getProfileImage(),
                            model.getImageUrl(),
                            model.getUid(),
                            model.getDescription(),
                            model.getId(),
                            model.getUsername(),
                            new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()),
                            snapshot.getTimestamp("timestamp"),
                            likeCountList
                    ));
                }
            }
            // Обновляем адаптер после того, как все данные добавлены
            adapter.notifyDataSetChanged();


        });

    }






}