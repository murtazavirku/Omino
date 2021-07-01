package com.vsn.omino.models;

public class SavedCollectionModel {

    String ImageUrl;
    String type;
    int Saction;
    String Cover;

    public SavedCollectionModel(String imageUrl, String type, int saction, String cover) {
        ImageUrl = imageUrl;
        this.type = type;
        Saction = saction;
        Cover = cover;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getSaction() {
        return Saction;
    }

    public void setSaction(int saction) {
        Saction = saction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        Cover = cover;
    }
}
