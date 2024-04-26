package com.example.destination.model;

import com.google.firebase.Timestamp;

public class MessageModel {
    private String message,senderId;
    private Timestamp time;


    public MessageModel() {
    }

    public MessageModel(String message, String senderId, Timestamp time) {
        this.message = message;
        this.senderId = senderId;
        this.time = time;
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
}
