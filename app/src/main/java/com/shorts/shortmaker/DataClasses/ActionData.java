package com.shorts.shortmaker.DataClasses;

import java.util.ArrayList;

import bolts.Bolts;

public class ActionData {

    private String title;
    private String description;
    private int iconPath;
    private ArrayList<String> data;
    /* True by default */
    private Boolean isActivated;


    public ActionData() {
    }

    public ActionData(String title, int iconPath) {
        this.title = title;
        this.iconPath = iconPath;
        isActivated = true;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIconPath(int iconPath) {
        this.iconPath = iconPath;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public int getIconPath() {
        return iconPath;
    }

    public Boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean activated) {
        isActivated = activated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
