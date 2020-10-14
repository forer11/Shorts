package com.shorts.shortmaker;

public class CustomItem {
    public String getSpinnerItemName() {
        return spinnerItemName;
    }

    public int getSpinnerItemImage() {
        return spinnerItemImage;
    }

    private  String spinnerItemName;
    private int spinnerItemImage;

    public CustomItem(String spinnerItemName, int spinnerItemImage) {
        this.spinnerItemName = spinnerItemName;
        this.spinnerItemImage = spinnerItemImage;
    }
}
