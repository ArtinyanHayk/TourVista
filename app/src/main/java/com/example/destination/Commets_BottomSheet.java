package com.example.destination;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.destination.adapter.CommentAdapter;
import com.example.destination.adapter.HomeAdapter;
import com.example.destination.databinding.CommentbottomsheetlayoutBinding;
import com.example.destination.model.CommentModel;
import com.example.destination.model.HomeModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Commets_BottomSheet extends BottomSheetDialogFragment {

    protected CommentbottomsheetlayoutBinding binding;
    private List<String> commentLikes;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private List<CommentModel> list;
    private CommentAdapter adapter;
    private String id,username;
    private String profileImage;





    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CommentbottomsheetlayoutBinding.inflate(getLayoutInflater());
        user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference Currentuser = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());
        Currentuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    username = (String) task.getResult().get("userName");
                    if ((String) task.getResult().get("imageURL") != null) {
                        profileImage = (String) task.getResult().get("imageURL");
                    }

                }
            }
        });





        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt("position");
             id = args.getString("id");
            String uid = args.getString("uid");
            String commentlist = args.getString("commentlist");

            // Теперь у вас есть доступ к переменным position, id, uid и commentlist
            // Вы можете использовать их в вашем коде здесь

            // Например, вы можете установить текст в EditText или выполнить другие действия
            // binding.commentEditText.setText(commentlist);
            binding.getRoot().post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
                    layoutParams.height = getResources().getDisplayMetrics().heightPixels; // Установите желаемую высоту или используйте другой размер
                    binding.getRoot().setLayoutParams(layoutParams);
                            }
            });
            binding.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "dimiss", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });

            Toast.makeText(getContext(), "!null", Toast.LENGTH_SHORT).show();
            binding.sendCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = binding.commentEditText.getText().toString();
                    if(comment == null){
                        Toast.makeText(getActivity(), "Empty comment", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Adapter
                    CollectionReference reference = FirebaseFirestore.getInstance()
                            .collection("userPosts").
                            document(id).
                            collection("comments");

                    String commentID = reference.document().getId();

                    Map<String,Object> map = new HashMap<>();
                    map.put("uid",user.getUid());
                    map.put("comment",comment);
                    map.put("commentID",commentID);
                    map.put("postID",id);
                    map.put("userName", username);
                    map.put("profileURL", profileImage);
                    map.put("commentLikes",commentLikes);

                    reference.document(commentID).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                binding.commentEditText.setText("");
                                Toast.makeText(getContext(), "+comment", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(), "Failed to comment" + task.getException()
                                        .getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
            });
        }









        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        list = new ArrayList<>();
        adapter = new CommentAdapter(list, getContext());
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
    @SuppressLint("NotifyDataSetChanged")
    private void loadDataFromFirestore() {
        /////////////////////////////////
        CollectionReference reference = FirebaseFirestore.getInstance().collection("userPosts")
                .document(id)
                .collection("comments");


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

                CommentModel model = snapshot.toObject(CommentModel.class);
                //assert geoPoint != null;
                //Toast.makeText(getContext(), geoPoint.toString(), Toast.LENGTH_SHORT).show();



                list.add(new CommentModel(
                        model.getUserName(),
                        model.getReplyComment(),
                        (String) snapshot.get("comment"),
                        (String) snapshot.get("profileURL"),
                        model.getUId(),
                        model.getLikeList(),
                        model.getTimestapmp()
                ));
            }
            // Обновляем адаптер после того, как все данные добавлены
            adapter.notifyDataSetChanged();


        });

    }




}


