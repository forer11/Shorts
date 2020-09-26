package com.example.shortmaker.DataClasses;

import java.util.ArrayList;

public class Icon {
    private String link;
    private String description;
    private ArrayList<String> category;

    public Icon() {
    }

    public Icon(String link, String description, ArrayList<String> category) {
        this.link = link;
        this.description = description;
        this.category = category;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }





}
