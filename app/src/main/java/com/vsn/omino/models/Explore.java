package com.vsn.omino.models;

public class Explore {

    String Channel_id;
    String Vibe_Channel_Name;
    String Image_lnk;
    String Userid;
    Boolean isSaved;


    public Explore(String Channel_id,String vibe_Channel_Name, String image_lnk,String Userid, Boolean isSaved) {
        this.Channel_id = Channel_id;
        Vibe_Channel_Name = vibe_Channel_Name;
        Image_lnk = image_lnk;
        this.isSaved = isSaved;
        this.Userid = Userid;
    }

    public String getChannel_id() {
        return Channel_id;
    }

    public void setChannel_id(String channel_id) {
        Channel_id = channel_id;
    }

    public String getVibe_Channel_Name() {
        return Vibe_Channel_Name;
    }

    public void setVibe_Channel_Name(String vibe_Channel_Name) {
        Vibe_Channel_Name = vibe_Channel_Name;
    }

    public String getImage_lnk() {
        return Image_lnk;
    }

    public void setImage_lnk(String image_lnk) {
        Image_lnk = image_lnk;
    }

    public Boolean getSaved() {
        return isSaved;
    }

    public void setSaved(Boolean saved) {
        isSaved = saved;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }
}
