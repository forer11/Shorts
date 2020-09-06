package com.example.shortmaker.DataClasses;

public class Shortcut {
    public Shortcut() {
        /* empty public constructor for FireStore */
    }

    public Shortcut(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    private String title;

    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
