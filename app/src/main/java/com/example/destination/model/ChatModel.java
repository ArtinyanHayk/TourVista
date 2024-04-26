package com.example.destination.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatModel {
    private String lastMessage,lastMsgSenderId;
    private List<String> membersId;
    private Timestamp lastMessageTime;

    public ChatModel() {
    }


    public ChatModel(String lastMessage,Timestamp lastMessageTime,  String lastMsgSenderId,List<String> membersId  ) {
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.lastMsgSenderId = lastMsgSenderId;
        this.membersId = membersId;


    }







    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }



    public List<String> getMembersId() {
        return membersId;
    }

    public void setMembersId(List<String> membersId) {
        this.membersId = membersId;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMsgSenderId() {
        return lastMsgSenderId;
    }

    public void setLastMsgSenderId(String lastMsgSenderId) {
        this.lastMsgSenderId = lastMsgSenderId;
    }
}
