package com.example.destination.model;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class MessageModel {
    private String message,senderId;
    List<String> imageUris;
    private Timestamp time;
    private String  profileImage,posterId;
    private String username;
    private GeoPoint location;



    public MessageModel() {
    }
    public MessageModel(String message, String senderId, String posterId, List<String> imageUris, Timestamp time, String profileImage, String username) {
        this.message = message;
        this.senderId = senderId;
        this.posterId = posterId;
        this.imageUris = imageUris;
        this.time = time;
        this.profileImage = profileImage;
        this.username = username;

    }

    public MessageModel(String message, String senderId, String posterId, List<String> imageUris, Timestamp time, String profileImage, String username, GeoPoint location) {
        this.message = message;
        this.senderId = senderId;
        this.posterId = posterId;
        this.imageUris = imageUris;
        this.time = time;
        this.profileImage = profileImage;
        this.username = username;
        this.location = location;

    }

    public MessageModel(String message, String senderId, Timestamp time, List<String>imageUris) {
        this.message = message;
        this.senderId = senderId;
        this.time = time;
        this.imageUris = imageUris;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public List<String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<String> imageUris) {
        this.imageUris = imageUris;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
