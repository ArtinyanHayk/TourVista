package com.example.destination.model;

import java.util.Date;

public class PostImageModel {
    private String imageUrl,id;
    private Date timeSetup;

    public PostImageModel(String imageUrl, String id, Date timeSetup) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.timeSetup = timeSetup;
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

    public Date getTimeSetup() {
        return timeSetup;
    }

    public void setTimeSetup(Date timeSetup) {
        this.timeSetup = timeSetup;
    }
}
