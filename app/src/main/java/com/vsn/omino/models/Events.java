package com.vsn.omino.models;

public class Events {

    String BackgroundImg;
    String EventName;
    String EventDate;
    String EventId;
    String GroupId;

    public Events(String backgroundImg, String eventName, String eventDate, String eventId, String groupId) {
        BackgroundImg = backgroundImg;
        EventName = eventName;
        EventDate = eventDate;
        EventId = eventId;
        GroupId = groupId;
    }

    public String getBackgroundImg() {
        return BackgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        BackgroundImg = backgroundImg;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }
}
