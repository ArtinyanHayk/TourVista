package com.example.destination.model;

import java.util.List;

public class ChatModel {
    private String name ,lastMessage,profilePic,lastMessageTime;
    private List<String> membersId;
    private int unseenMessage;

    public ChatModel() {
    }
    public ChatModel( String lastMessage, List<String> membersId,String lastMessageTime) {
        this.lastMessage = lastMessage;
        this.membersId = membersId;
        this.lastMessageTime = lastMessageTime;
    }

    public ChatModel(String name, String lastMessage, String profilePic, List<String> membersId, int unseenMessage) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.membersId = membersId;
        this.unseenMessage = unseenMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnseenMessage() {
        return unseenMessage;
    }

    public void setUnseenMessage(int unseenMessage) {
        this.unseenMessage = unseenMessage;
    }

    public List<String> getMembersId() {
        return membersId;
    }

    public void setMembersId(List<String> membersId) {
        this.membersId = membersId;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
