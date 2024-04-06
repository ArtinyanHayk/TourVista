package com.example.destination;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.destination.databinding.CommentbottomsheetlayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class Commets_BottomSheet extends BottomSheetDialogFragment {

    private CommentbottomsheetlayoutBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CommentbottomsheetlayoutBinding.inflate(getLayoutInflater());
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
                dismiss();
            }
        });




        return binding.getRoot();


    }


}
