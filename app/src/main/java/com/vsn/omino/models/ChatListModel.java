package com.vsn.omino.models;


public class ChatListModel {
    String lastMsg;
    String newMsgs;
    String userID;
    Boolean isBadge;

    public ChatListModel(String lastMsg, String newMsgs, String userID, Boolean isBadge) {
        this.lastMsg = lastMsg;
        this.newMsgs = newMsgs;
        this.userID = userID;
        this.isBadge = isBadge;
    }


    public String getLastMsg() {
        return lastMsg;
    }

    public String getNewMsgs() {
        return newMsgs;
    }


    public String getUserID() {
        return userID;
    }

    public Boolean getIsBadgeVisible() {
        return isBadge;
    }

}
