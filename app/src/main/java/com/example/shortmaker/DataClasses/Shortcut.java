package com.example.shortmaker.DataClasses;


import com.example.shortmaker.Actions.Action;

import java.util.ArrayList;

public class Shortcut {

    private String title;

    private String imageUrl;

    private String id;

    private int pos;

    private ArrayList<Action> actions;

    public Shortcut() {
        /* empty public constructor for FireStore */
    }

    public Shortcut(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        actions = new ArrayList<>();
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}
