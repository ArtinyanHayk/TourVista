package com.example.destination.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.destination.model.UserModel;

public class AndroidUtil {
   public  static void showToast(Context context, String message){
       Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void passUserModelAsIntent(Intent intent, UserModel userModel){
       intent.putExtra("userName", userModel.getUserName());
        intent.putExtra("phone", userModel.getPhone());
        intent.putExtra("userId", userModel.getUserId());
    }
    public static UserModel getUserModelFromIntent(Intent intent) {
        UserModel usermodel = new UserModel();
        usermodel.setUserName(intent.getStringExtra("userName"));
        usermodel.setPhone(intent.getStringExtra("phone"));
        usermodel.setUserId(intent.getStringExtra("userId"));
        return usermodel;
    }
    public static  void setProfilPic(Context context, Uri imageUri, ImageView imageView ){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }


}
