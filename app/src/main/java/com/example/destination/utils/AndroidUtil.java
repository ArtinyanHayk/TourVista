package com.example.destination.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.Window;
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
    public static  void setPostPic(Context context, Uri imageUri, ImageView imageView ){
        Glide.with(context).load(imageUri).into(imageView);
    }
    public static boolean isGPSEnabled(Context context) {

        if (context != null) {
            try {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                Log.e("isGpsEnabled/NetworkFragment", "Ошибка при проверке GPS: " + e.getMessage());
                return false;
            }
        } else {
            Log.e("isGpsEnabled/NetworkFragment", "Контекст равен null");
            return false;
        }
    }
    public static void showGPSEnableDialog(Context context) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Для просмотра местоположения включите GPS")
                    .setCancelable(false)
                    .setPositiveButton("Включить GPS", (dialog, id) -> context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            Window window = alert.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            alert.show();
        } else {
            Log.e("showGPSEnableDialog", "Контекст равен null");
        }
    }




}
