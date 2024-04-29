package com.example.destination.model;

import android.net.Uri;

import com.google.firebase.Timestamp;

import java.util.List;

public class MessageModel {
    private String message,senderId;
    List<String> imageUris;
    private Timestamp time;


    public MessageModel() {
    }

    public MessageModel(String message, String senderId, Timestamp time,List<String>imageUris) {
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
}
