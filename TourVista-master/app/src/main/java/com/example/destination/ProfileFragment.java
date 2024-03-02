package com.example.destination;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private TextView nameTv, statusTv, followersCountTv, postCountTv, reitingCountTv;
    private CircleImageView profileImage;
    private Button followBtn;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dusa qcm

    }

    public ProfileFragment() {

    }

    @SuppressLint({"CutPasteId", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        return inflater.inflate(R.layout.activity_profile_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }
    public void init(View view){
        //sra aydin ushadir
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        assert getActivity() != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //
        nameTv = view.findViewById(R.id.nameTv);
        statusTv = view.findViewById(R.id.statusTV);
        postCountTv = view.findViewById(R.id.postsCountTv);
        followersCountTv = view.findViewById(R.id.folowersCountTv);
        reitingCountTv= view.findViewById(R.id.ratingTv);
        profileImage = view.findViewById(R.id.profile_image_view);
        followBtn = view.findViewById(R.id.followbtn);
        recyclerView= view.findViewById(R.id.recyclerView);


    }


}
