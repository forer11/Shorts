package com.example.shortmaker.Actions;

import android.content.Context;


import com.example.shortmaker.DataClasses.Action;

public class ActionPhoneCall implements Action {

    private static final int REQUEST_CALL = 1;
    public static final String PHONE_CALL_DIALOG_TITLE = "Make a phone call";
    public static final String PHONE_CALL_DIALOG_POS_BTN = "DIAL";

    private Context context;

    public ActionPhoneCall(Context context) {
        this.context=context;
    }


    @Override
    public void activate() {

    }

    @Override
    public void setData(String stringData, Integer intData) {

    }
}
