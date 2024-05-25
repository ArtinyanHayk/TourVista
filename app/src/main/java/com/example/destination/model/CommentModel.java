package com.example.destination.model;

import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class CommentModel {
    private String userName,  replyComment, commentText, profileImage, Uid,commentId;
    private List<String> likeList;
    @ServerTimestamp
    private Date timestapmp;

    public CommentModel() {
    }

    public CommentModel(String userName,  String replyComment, String commentText, String profileImage, String Uid, List<String> likeList, Date timestapmp) {
        this.userName = userName;
        this.replyComment = replyComment;
        this.commentText = commentText;
        this.profileImage = profileImage;
        this.Uid = Uid;
        this.likeList = likeList;
        this.timestapmp = timestapmp;
    }

    public CommentModel(String commentText, String commentId,List<String> likeList, String replyComment, String profileImage,String uid,String userName,    Date timestapmp) {
        this.userName = userName;
        this.replyComment = replyComment;
        this.commentText = commentText;
        this.profileImage = profileImage;
        this.Uid = uid;
        this.commentId = commentId;
        this.likeList = likeList;
        this.timestapmp = timestapmp;
    }





    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUId() {
        return Uid;
    }

    public void setUId(String id) {
        this.Uid = id;
    }

    public List<String> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }

    public Date getTimestapmp() {
        return timestapmp;
    }

    public void setTimestapmp(Date timestapmp) {
        this.timestapmp = timestapmp;
    }
}

