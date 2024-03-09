package com.example.destination;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.destination.adapter.HomeAdapter;
import com.example.destination.model.HomeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkFragment extends Fragment {
    private RecyclerView recyclerView;
    HomeAdapter adapter;
    private List<HomeModel> list;
    private FirebaseUser user;
    DocumentReference reference;
    Date curent_date;

    public NetworkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.network_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        curent_date = new Date();

        if (user != null) {
            reference = FirebaseFirestore.getInstance().collection("Posts").document(user.getUid());
        } else {
            // Обработка случая, когда пользователь не аутентифицирован
            // Можно взять другие меры, например, перенаправить на экран входа
        }
        list = new ArrayList<>();
        adapter = new HomeAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


    }

    private void loadDataFromFirestore() {
        //  if (reference != null) {
        //     list.add(new HomeModel("Marsed",String.valueOf(curent_date),"", "","1234",333));
        //      list.add(new HomeModel("TourVista","01/12/2024","", "","1234",333));
        //      list.add(new HomeModel("Karen","01/12/2024","", "","1234",333));
        //      list.add(new HomeModel("Hayk","01/12/2024","", "","1234",333));
        //      adapter.notifyDataSetChanged();
        //  } else {
//
        //  }
       CollectionReference reference = FirebaseFirestore.getInstance().collection("users")
               .document(user.getUid())
               .collection("Post Images");

       reference.addSnapshotListener( new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               if (error != null) {
                  Log.e("Error:", error.getMessage());
                   return;
               }
               assert value != null;

              for (QueryDocumentSnapshot snapshot : value) {

              }


           }


       });
        adapter.notifyDataSetChanged();

    }
}
