package com.example.shortmaker.DataClasses;

import java.util.ArrayList;

public class ActionData {

    private String title;
    private int iconPath;
    public ArrayList<String> data;


    public ActionData() {
    }

    public ActionData(String title, int iconPath) {
        this.title = title;
        this.iconPath = iconPath;
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
}
