package com.example.destination;

import android.os.Bundle;
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

import com.example.destination.databinding.CommentbottomsheetlayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Commets_BottomSheet extends BottomSheetDialogFragment {

    protected CommentbottomsheetlayoutBinding binding;
    private List<String> commentLikes;
    private FirebaseUser user;




    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CommentbottomsheetlayoutBinding.inflate(getLayoutInflater());
        user = FirebaseAuth.getInstance().getCurrentUser();



        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt("position");
            String id = args.getString("id");
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



}


