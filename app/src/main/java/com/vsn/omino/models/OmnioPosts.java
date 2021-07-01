package com.vsn.omino.models;

public class OmnioPosts {

    String caption;
    String category;
    String color;
    String dataURL;
    String datetimepost;
    String isanalytics;
    String isnft;
    String tags;
    String userid;
    String usermail;
    String usernamee;
    String coverart;
    String postid;
    String groupid;

    public OmnioPosts(String caption, String category, String color, String dataURL, String datetimepost, String isanalytics, String isnft, String tags, String userid, String usermail, String usernamee, String coverart, String postid, String groupid) {
        this.caption = caption;
        this.category = category;
        this.color = color;
        this.dataURL = dataURL;
        this.datetimepost = datetimepost;
        this.isanalytics = isanalytics;
        this.isnft = isnft;
        this.tags = tags;
        this.userid = userid;
        this.usermail = usermail;
        this.usernamee = usernamee;
        this.coverart = coverart;
        this.postid = postid;
        this.groupid = groupid;
    }

    public String getCoverart() {
        return coverart;
    }

    public void setCoverart(String coverart) {
        this.coverart = coverart;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDataURL() {
        return dataURL;
    }

    public void setDataURL(String dataURL) {
        this.dataURL = dataURL;
    }

    public String getDatetimepost() {
        return datetimepost;
    }

    public void setDatetimepost(String datetimepost) {
        this.datetimepost = datetimepost;
    }

    public String getIsanalytics() {
        return isanalytics;
    }

    public void setIsanalytics(String isanalytics) {
        this.isanalytics = isanalytics;
    }

    public String getIsnft() {
        return isnft;
    }

    public void setIsnft(String isnft) {
        this.isnft = isnft;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getUsernamee() {
        return usernamee;
    }

    public void setUsernamee(String usernamee) {
        this.usernamee = usernamee;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
