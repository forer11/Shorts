package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.ReadAutoDialog;

import java.util.List;

public class ActionReadAuto implements Action {
    private ReadAutoDialog dialog;
    private int turnOn;


    public ActionReadAuto() {
        this.dialog = new ReadAutoDialog();
    }

    @Override
    public void activate(Context context, Activity activity) {

    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        turnOn = Integer.parseInt(data.get(0));
    }
}
