package com.vsn.omino.models;

public class VibeChannelDataModel {

    String Category;
    String DataCover;
    String DataUrl;

    public VibeChannelDataModel(String category, String dataCover, String dataUrl) {
        Category = category;
        DataCover = dataCover;
        DataUrl = dataUrl;
    }

    public String getCategory() {
        return Category;
    }

    public String getDataCover() {
        return DataCover;
    }

    public String getDataUrl() {
        return DataUrl;
    }
}
