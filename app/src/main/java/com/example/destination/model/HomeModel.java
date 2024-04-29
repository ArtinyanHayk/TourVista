package com.example.destination.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class HomeModel {
    ///timestapmp
    private String  profileImage, imageUrl, uid, description, id;
    private String username;
    private LatLng location;
    private Timestamp postedTime;
     private List<String> likes;


    public HomeModel() {
    }
    public HomeModel(String profileImage, String imageUrl, String uid, String description, String id, String username,  Timestamp postedTime, List<String> likes) {
        this.profileImage = profileImage;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.description = description;
        this.id = id;
        this.username = username;
        this.postedTime = postedTime;
        this.likes = likes;
    }

    public HomeModel(String profileImage, String imageUrl, String uid, String description, String id, String username, LatLng location, Timestamp postedTime, List<String> likes) {
        this.profileImage = profileImage;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.description = description;
        this.id = id;
        this.username = username;
        this.location = location;
        this.postedTime = postedTime;
        this.likes = likes;
    }


    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(Timestamp postedTime) {
        this.postedTime = postedTime;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
}



