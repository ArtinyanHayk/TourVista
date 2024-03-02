package com.example.destination;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.destination.adapter.HomeAdapter;
import com.example.destination.model.HomeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class NetworkFragment extends Fragment {
    private RecyclerView recyclerView;
    HomeAdapter adapter;
    private List<HomeModel> list;
    // nor ban??
    private FirebaseUser user;
    DocumentReference reference;


    public NetworkFragment() {

    }
    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

       return inflater.inflate(R.layout.network_fragment, container, false);
   }
   public void onViewCreated(@NonNull View view,  Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
       init(view);
       //dusa qcm erb // hanm es.. avelacra add location fragmentum u
       //stex inflater.inflate(R.layout.network_fragment, container, false)


//KARA STEXIC DURS QCI/ZORV
   reference = FirebaseFirestore.getInstance().collection("Posts").document(user.getUid());
    list = new ArrayList<>();
    adapter = new HomeAdapter(list,getContext());
    recyclerView.setAdapter(adapter);
    loadDataFromFirestore();


    }
    private void init(View view){
        //KARA STEXIC DURS QCI/ZOROV
       recyclerView = view.findViewById(R.id.recyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       FirebaseAuth auth = FirebaseAuth.getInstance();
       user = auth.getCurrentUser();
    }

    private void loadDataFromFirestore(){
      // list.add(new HomeModel("TourVista","30/01/24","","","123456",2007,"FoolYere1","InYere1"));
    //   list.add(new HomeModel("TourVista","30/01/24","","","123457",2007));
    //   list.add(new HomeModel("TourVista","31/01/24","","","123458",15));
    //   list.add(new HomeModel("TourVista","01/02/24","","","123459",1000));
//??
        adapter.notifyDataSetChanged();
    }


}