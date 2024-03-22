package com.example.destination.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class HomeModel {
    ///timestapmp
    private String username,  profileImage, imageUrl, uid, description, tag, comments,id;
    @ServerTimestamp
    private Date timestapmp;
    private List<String> likes;


    public HomeModel() {
    }

    public HomeModel(String username, String profileImage, String imageUrl, String uid, String description, String tag, String comments, String id, Date timestapmp, List<String> likes) {
        this.username = username;
        this.profileImage = profileImage;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.description = description;
        this.tag = tag;
        this.comments = comments;
        this.id = id;
        this.timestapmp = timestapmp;
        this.likes = likes;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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


}



