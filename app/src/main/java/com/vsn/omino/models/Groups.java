package com.vsn.omino.models;

public class Groups {

    String groupIcon;
    String groupName;
    String groupNoSubs;
    String groupNewPosts;
    Boolean Badge;
    String groupID;
    String groupDesc;

    public Groups(String groupIcon, String groupName, String groupNoSubs, String groupNewPosts, Boolean badge, String groupID, String groupDesc) {
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.groupNoSubs = groupNoSubs;
        this.groupNewPosts = groupNewPosts;
        Badge = badge;
        this.groupID = groupID;
        this.groupDesc = groupDesc;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupNoSubs() {
        return groupNoSubs;
    }

    public void setGroupNoSubs(String groupNoSubs) {
        this.groupNoSubs = groupNoSubs;
    }

    public String getGroupNewPosts() {
        return groupNewPosts;
    }

    public void setGroupNewPosts(String groupNewPosts) {
        this.groupNewPosts = groupNewPosts;
    }

    public Boolean getBadge() {
        return Badge;
    }

    public void setBadge(Boolean badge) {
        Badge = badge;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}
