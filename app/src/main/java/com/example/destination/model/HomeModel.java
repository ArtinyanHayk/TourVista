package com.example.destination.model;

import com.google.firebase.Timestamp;

public class HomeModel {
    private String username, timestapmp, profileImage, postImage, uid, description, tag, comments;
    private int likeCount;

    public HomeModel() {
    }

    public HomeModel(String username, String timestapmp, String profileImage, String postImage, String uid, String description, String tag, String comments, int likeCount) {
        this.username = username;
        this.timestapmp = timestapmp;
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.uid = uid;
        this.description = description;
        this.tag = tag;
        this.comments = comments;
        this.likeCount = likeCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestapmp() {
        return timestapmp;
    }

    public void setTimestapmp(String timestapmp) {
        this.timestapmp = timestapmp;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
