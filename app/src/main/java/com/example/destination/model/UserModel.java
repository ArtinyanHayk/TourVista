package com.example.destination.model;

import android.widget.ImageView;

import androidx.room.Entity;

import com.google.firebase.Timestamp;

import java.util.List;

public class UserModel {
    private String phone;
    private String userName;
    private Timestamp createdTimesetap;
    private String userId;
    //// UserModel_um kan u getter setter
   private List<String> followers,following;

   private String imageURL;
   private Boolean online;
    ////
 //   private ImageView imageView;

    public UserModel() {
    }

    public UserModel(String phone, String userName, Timestamp createdTimesetap,String userId,List<String> followers,List<String> following,String imageURL,Boolean online
    ) {
        this.phone = phone;
        this.userName = userName;
        this.createdTimesetap = createdTimesetap;
        this.userId =  userId;
        ////
       this.following = following;
       this.followers = followers;
       this.imageURL =  imageURL;
       this.online = online;

        ////
    }

    public UserModel( String userName,List<String> followers,  List<String> following,   String userId, String imageURL) {
        this.userName = userName;
        this.followers = followers;
        this.following = following;
        this.userId = userId;
        this.imageURL = imageURL;

    }




    public boolean getonline() {return online;}
    public void setonline(boolean online) {this.online = online;}

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

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
