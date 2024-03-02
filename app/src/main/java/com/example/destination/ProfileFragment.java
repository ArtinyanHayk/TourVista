package com.example.destination;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.destination.model.PostImageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private TextView nameTv, statusTv, followersCountTv, postCountTv, folowingCountTv, reitingCountTv;
    private CircleImageView profileImage;
    private Button followBtn;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private RelativeLayout folloeLayout;
    private LinearLayout countLayout;
    boolean isMyProfile = true;
    FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;
    String uid;


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

        if (isMyProfile) {
            followBtn.setVisibility(view.GONE);
            countLayout.setVisibility(view.VISIBLE);
        } else {
            followBtn.setVisibility(view.VISIBLE);
            countLayout.setVisibility(view.GONE);
        }

        loadBasicData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

       // loadPostsImages();
    }

    public void init(View view) {
        //sra aydin ushadir
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        assert getActivity() != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //
        nameTv = view.findViewById(R.id.nameTv);
        statusTv = view.findViewById(R.id.statusTV);
        postCountTv = view.findViewById(R.id.postsCountTv);
        followersCountTv = view.findViewById(R.id.folowersCountTv);

        reitingCountTv = view.findViewById(R.id.ratingTv);
        profileImage = view.findViewById(R.id.profile_image_view);
        followBtn = view.findViewById(R.id.followbtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //
        countLayout = view.findViewById(R.id.countLayout);

    }

    public void loadBasicData() {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value.exists()) {
                    ////
                    String userName = value.getString("userName");
                    int folowing = value.getLong("folowing").intValue();
                    int folowers = value.getLong("folowers").intValue();
                    //[VIDEO] 9 kara dus qci staus = null- ic u folowing bayc sra vra petq chi shat ushadr. dardznel
                    //String status = value.getString("status");
                    String profileURL = value.getString("profileImage");
                    //     //reting@ chka u eli shat baner
                    //folowing obyekt ka sarqac bayc folowingi dasht chka
                    nameTv.setText(userName);
                    //   statusTv.setText(status);
                    followersCountTv.setText((String.valueOf(folowers)));
                    //Glide.with(getContext().getApplicationContext())
                    //        .load(profileURL)
                    //        .placeholder(R.drawable.ic_person)
                    //        .timeout(6500)
                    //        .into(profileImage);
//


                }
            }

        });
    }
//STEX KAM SXAL KAM EL NRA HAMARA DUS QCM VOR NKAR CHKA
    //KARELIA NAYEL VONCA INQ@ NKARI URL STANM
  // private void loadPostsImages() {
  //     if (isMyProfile) {
  //         uid = user.getUid();
  //     }else{

  //     }
  //     DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(uid);
  //     Query query = reference.collection("Images");
  //     FirestoreRecyclerOptions<PostImageModel> options = new FirestoreRecyclerOptions.Builder<PostImageModel>()
  //             .setQuery(query, PostImageModel.class)
  //             .build();
  //         adapter = new FirestoreRecyclerAdapter<PostImageModel, PostImageHolder>(options) {
  //         @NonNull
  //         @Override
  //         public PostImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
  //             View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_items, parent, false);
  //             return  new PostImageHolder(view);
  //         }

  //         @Override
  //         protected void onBindViewHolder(@NonNull PostImageHolder holder, int position, @NonNull PostImageModel model) {
  //             Glide.with(holder.itemView.getContext().getApplicationContext())
  //                     .load(model.getImageUrl())
  //                     .timeout(6500)
  //                     .into(holder.imageView);
  //         }
  //     };


  // }


    private  class PostImageHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);

            //Videoumela sxal sharunakutyun@ nayi 11rd videoi
            //Videoi mej collectiona sarqm henc firbaseic eti sarqaca erevi te sxal akaunti mot
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
//Stex sxal chka uxxaki esi kaxvaca sxalov metodic
 //  @Override
 //  public void onStop() {
 //      super.onStop();
 //      adapter.stopListening();
 //  }

 // @Override
 // public void onStart() {
 //     super.onStart();
 //     adapter.startListening();
 // }
}
