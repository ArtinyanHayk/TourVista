package com.example.destination.model;

import java.util.Date;

public class PostImageModel {
    private String imageUrl, id, description;
    private Date timeStamp;

    public PostImageModel() {
    }

    public PostImageModel(String imageUrl, String id, String description, Date timeSetup) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.description = description;
        this.timeStamp = timeSetup;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeSetup) {
        this.timeStamp = timeSetup;
    }
}