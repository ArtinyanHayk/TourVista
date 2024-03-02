package com.example.destination;

import static com.example.destination.utils.FirbaseUtil.logout;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.destination.R;
import com.example.destination.model.HomeModel;
import com.example.destination.model.UserModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.FirbaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Add_location_Fragment extends Fragment {

    Uri selectedImageUri;
    ActivityResultLauncher<Intent> imagePickLaunch;
    ImageButton postpick;
    EditText description;
    EditText tags;
    ProgressBar progressBar;
    ImageButton post;
    HomeModel currentPostModel;



    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        imagePickLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndroidUtil.setPostPic(getApplicationContext(), selectedImageUri, postpick);
                        }
                    }
                });


    }

    public Add_location_Fragment() {

    }

    @SuppressLint({"CutPasteId", "MissingInflatedId", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_location_fragment, container, false);
    description = view.findViewById(R.id.description);
    tags = view.findViewById(R.id.add_tags);
    tags = view.findViewById(R.id.add_tags);
    postpick = view.findViewById(R.id.add_image_view);
    progressBar = view.findViewById(R.id.add_progress_bar);
    post = view.findViewById(R.id.add_post);
    postpick.setOnClickListener(v -> {
         ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512).
                 createIntent(new Function1<Intent, Unit>() {
                     @Override
                     public Unit invoke(Intent intent) {
                         imagePickLaunch.launch(intent);
                         return null;
                     }
                 });
     });
     post.setOnClickListener(v -> {
         postBtnClick();

     });


        return view;
    }
  void postBtnClick() {
      currentPostModel = new HomeModel();
      String currentDescription = description.getText().toString();
      if (currentDescription.isEmpty() || currentDescription.length() < 3) {
          description.setError("Username length should be  at least 3 chars");
          return;
      }

      setInProgress(true);
      if (selectedImageUri != null) {
          FirbaseUtil.getCurrentPostPicStorageRef().putFile(selectedImageUri).addOnCompleteListener(
                  task -> {
                      //esi vor chjnjem sxala talis
                      //updateToFirestore();
                  }
          );
      } else {
          //esi vor chjnjem sxala talis
         // updateToFirestore();
      }


  }
  @SuppressLint("RestrictedApi")
  void updateToFirestore() {
      FirbaseUtil.currentPostsDetails().set(currentPostModel).addOnCompleteListener(task -> {
          setInProgress(false);
          if (task.isSuccessful()) {
              AndroidUtil.showToast(getApplicationContext(), "Updated successfully");
          } else {
              AndroidUtil.showToast(getApplicationContext(), "Updat failed");
          }

      });

  }
  void setInProgress(boolean inProgress) {
      if (inProgress) {
          progressBar.setVisibility(View.VISIBLE);
      } else {
          progressBar.setVisibility(View.GONE);
      }
  }

}

