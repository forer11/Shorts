package com.example.shortmaker.DataClasses;


import com.example.shortmaker.Actions.Action;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Shortcut {

    private String title;

    private String imageUrl;

    private String id;

    private int pos;

    private ArrayList<ActionData> actionDataList;

    @Exclude
    private ArrayList<Action> actions;

    public Shortcut() {
        /* empty public constructor for FireStore */
    }

    public Shortcut(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        actions = new ArrayList<>();
        actionDataList = new ArrayList<>();
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

    @Exclude
    public ArrayList<Action> getActions() {
        return actions;
    }

    @Exclude
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public ArrayList<ActionData> getActionDataList() {
        return actionDataList;
    }

    public void setActionDataList(ArrayList<ActionData> actionDataList) {
        this.actionDataList = actionDataList;
    }
}
