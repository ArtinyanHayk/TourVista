package com.example.destination.Fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.destination.Location.LocationForUser;
import com.example.destination.R;
import com.example.destination.adapter.HomeAdapter;
import com.example.destination.databinding.ActivityMainBinding;
import com.example.destination.model.HomeModel;
import com.example.destination.Activityes.search_Activity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
    private List<String> commentLikes;
    ImageButton search_user;
    Date curent_date;
    String Time;
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
        /////////////////////////////////
       list222 = new ArrayList<>();
       //commentLikes = new ArrayList<>();
       /////////////////////////////////
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
            public void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked) {
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

                    Commets_BottomSheet fragment = new Commets_BottomSheet();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putString("id", id);
                    args.putString("uid", uid);
                    fragment.setArguments(args);
                    fragment.show(getActivity().getSupportFragmentManager(), "comment bottom sheet dialog");



            }


            @Override
            public void onGetLocation(int position, String id, String uid, LatLng location) {
                if(isGPSEnabled()) {
                    Toast.makeText(getContext(), Double.toString(location.latitude) + "  " + Double.toString(location.longitude), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LocationForUser.class);
                    intent.putExtra("Location", location);
                    startActivity(intent);
                }
                else{
                    showGPSEnableDialog();
                }


            }
        });
    }
//  // private void showBottomDialog() {
///
//  //     final Dialog dialog = new Dialog(this);
//  //     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//  //     dialog.setContentView(R.layout.bottomsheetlayout);
///
//  //     LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
//  //     LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
//  //     LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
//  //     ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

//   private void showDialog() {
//       final Dialog dialog = new Dialog(getActivity());
//       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//       dialog.setContentView(R.layout.commentbottomsheetlayout);
//       RelativeLayout EditTextt  = dialog.findViewById(R.id.comment_edit_text);
//       //stex dra mechi baner@ pti lni voncor EDITTEXT SENDBUTTON
//       dialog.show();
//       dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//       dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//       dialog.getWindow().setGravity(Gravity.BOTTOM);
//   }

    private  boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void showGPSEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("To view the location, turn on GPS")
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
                // if( snapshot.getDouble("location latitude") == 0.0){
                //     Log.e("double111","null");
                // }

                HomeModel model = snapshot.toObject(HomeModel.class);
                GeoPoint geoPoint = (GeoPoint) snapshot.get("Location");
               //assert geoPoint != null;
               //Toast.makeText(getContext(), geoPoint.toString(), Toast.LENGTH_SHORT).show();




                if(geoPoint == null){

                    list.add(new HomeModel(

                            model.getProfileImage(),
                            model.getImageUrl(),
                            model.getUid(),
                            model.getDescription(),
                            model.getId(),
                            model.getUsername(),
                            (String) snapshot.get("timestamp"),
                            (List<String>) snapshot.get("likeCount")
                    ));
                }
                else {
                    list.add(new HomeModel(

                            model.getProfileImage(),
                            model.getImageUrl(),
                            model.getUid(),
                            model.getDescription(),
                            model.getId(),
                            model.getUsername(),
                            new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()),
                            (String) snapshot.get("timestamp"),
                            (List<String>) snapshot.get("likeCount")
                    ));
                }
            }
            // Обновляем адаптер после того, как все данные добавлены
            adapter.notifyDataSetChanged();


        });

    }






}