package com.example.shortmaker.DataClasses;

public class ActionObj {

    String title;
    int iconPath;

    public ActionObj(String title, int iconPath) {
        this.title = title;
        this.iconPath = iconPath;
    }

    public String getTitle() {
        return title;
    }

    public int getIconPath() {
        return iconPath;
    }
}
