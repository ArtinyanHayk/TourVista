package com.example.destination;

<<<<<<< Updated upstream
import static com.example.destination.utils.ImageContent.loadSavedImages;
=======
import static com.example.destination.utils.FirbaseUtil.logout;
>>>>>>> Stashed changes

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

<<<<<<< Updated upstream
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
=======
import com.example.destination.R;
import com.example.destination.model.UserModel;
import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.FirbaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
>>>>>>> Stashed changes

import com.example.destination.adapter.GalleryAdapter;
import com.example.destination.model.GalleryImages;

import java.util.ArrayList;
import java.util.List;

public class Add_location_Fragment extends Fragment {

<<<<<<< Updated upstream
    EditText descET;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ImageButton backBtn, nextBtn;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;
=======




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
>>>>>>> Stashed changes

    public Add_location_Fragment() {

    }

    @SuppressLint({"CutPasteId", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
<<<<<<< Updated upstream
        return inflater.inflate(R.layout.activity_add_location_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    private void init(View view) {
        descET = view.findViewById(R.id.descriptionET);
        imageView = view.findViewById(R.id.imageView);
        backBtn = view.findViewById(R.id.back_btn);
        nextBtn = view.findViewById(R.id.next_btn);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadSavedImages(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
                adapter.notifyDataSetChanged();
            }
        });
    }
}


=======


        return null;
    }
}
>>>>>>> Stashed changes
