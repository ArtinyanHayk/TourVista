package com.example.destination.model;

import android.widget.ImageView;

import androidx.room.Entity;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phone;
    private String userName;
    private Timestamp createdTimesetap;
    private String userId;
    //// UserModel_um kan u getter setter
   int folowers;
   int folowing;
   String imageURL;
   String status;
    ////
 //   private ImageView imageView;

    public UserModel() {
    }

    public UserModel(String phone, String userName, Timestamp createdTimesetap,String userId,int folowers,String imageURL,int folowing,String status
    ) {
        this.phone = phone;
        this.userName = userName;
        this.createdTimesetap = createdTimesetap;
        this.userId =  userId;
        ////
       this.folowing = folowing;
       this.folowers = folowers;
       this.imageURL =  imageURL;
       this.status = status;
        ////
    }
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public int getFolowers() {return folowers;}
    public void setFolowers(int folowers) {this.folowers = folowers;}
    public int getFolowing() {return folowing;}
    public void setFolowing(int folowing) {this.folowing = folowing;}
    public String getImageURL() {return imageURL;}
    public void setImageURL(String imageURL) {this.imageURL = imageURL;}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreatedTimesetap() {
        return createdTimesetap;
    }

    public void setCreatedTimesetap(Timestamp createdTimesetap) {
        this.createdTimesetap = createdTimesetap;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

 //   public ImageView getImageView() {
 //       return imageView;
 //   }
//
 //   public void setImageView(ImageView imageView) {
 //       this.imageView = imageView;
    //}
}
