package com.example.destination.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class HomeModel {
    ///timestapmp
    private String  profileImage, imageUrl, uid, description, comments,id;
    private String username;
    private double latitude;
    private double longitude;
    @ServerTimestamp
    private Date timestapmp;
     private List<String> likes;


    public HomeModel() {
    }

    public HomeModel(String profileImage, String imageUrl, String uid, String description, String comments, String id, String username, double latitude, double longitude, Date timestapmp, List<String> likes) {
        this.profileImage = profileImage;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.description = description;
        this.comments = comments;
        this.id = id;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestapmp = timestapmp;
        this.likes = likes;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestapmp() {
        return timestapmp;
    }

    public void setTimestapmp(Date timestapmp) {
        this.timestapmp = timestapmp;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
}



