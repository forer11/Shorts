package com.example.shortmaker.DataClasses;

import android.graphics.drawable.Drawable;

public class Shortcut {

    private boolean tintNeeded;

    private String title;

    private String imageUrl;

    public Shortcut() {
        /* empty public constructor for FireStore */
    }

    public Shortcut(String title, String imageUrl, boolean tintNeeded) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.tintNeeded = tintNeeded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public boolean isTintNeeded() {
        return tintNeeded;
    }

    public void setTintNeeded(boolean tintNeeded) {
        this.tintNeeded = tintNeeded;
    }
}
