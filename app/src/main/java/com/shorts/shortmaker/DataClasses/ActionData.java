package com.shorts.shortmaker.DataClasses;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;

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

    // not a real copy constructor
    @Exclude
    public void copyAction(ActionData actionData) {
        this.title = actionData.title;
        this.iconPath = actionData.iconPath;
        this.copyData(actionData.data);
        this.isActivated = actionData.isActivated;
        this.description = actionData.description;
    }

    public ActionData(String title, int iconPath) {
        this.title = title;
        this.iconPath = iconPath;
        isActivated = true;
        allocData();
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

    public void copyData(ArrayList<String> data) {
        allocData();
        this.data.clear();
        this.data.addAll(data);
    }

    @Exclude
    private void allocData() {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
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
