package com.example.destination.model;

public class MessageModel {
    private String message,senderId,Time;


    public MessageModel() {
    }

    public MessageModel(String message, String senderId, String time) {
        this.message = message;
        this.senderId = senderId;
        Time = time;
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
