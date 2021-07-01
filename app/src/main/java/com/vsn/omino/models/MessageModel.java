package com.vsn.omino.models;

public class MessageModel {

    String Sender;
    String Receiver;
    String MsgType;
    String Content;
    String DateTime;
    String isSeen;
    String MsgID;

    public MessageModel(String sender, String receiver, String msgType, String content, String dateTime, String isSeen, String msgID) {
        Sender = sender;
        Receiver = receiver;
        MsgType = msgType;
        Content = content;
        DateTime = dateTime;
        this.isSeen = isSeen;
        MsgID = msgID;
    }


    public String getSender() {
        return Sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public String getMsgType() {
        return MsgType;
    }

    public String getContent() {
        return Content;
    }

    public String getDateTime() {
        return DateTime;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public String getMsgID() {
        return MsgID;
    }
}
