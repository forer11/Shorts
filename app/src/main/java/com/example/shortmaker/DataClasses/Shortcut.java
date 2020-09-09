package com.example.shortmaker.DataClasses;

import android.graphics.drawable.Drawable;

public class Shortcut {
    public Shortcut() {
        /* empty public constructor for FireStore */
    }

    public Shortcut(String title, Drawable drawable, boolean tintNeeded) {
        this.title = title;
        this.drawable = drawable;
        this.tintNeeded = tintNeeded;
    }
    
    private boolean tintNeeded;

    private String title;

    private Drawable drawable;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public boolean isTintNeeded() {
        return tintNeeded;
    }

    public void setTintNeeded(boolean tintNeeded) {
        this.tintNeeded = tintNeeded;
    }
}
