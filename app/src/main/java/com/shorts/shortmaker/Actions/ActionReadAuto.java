package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.ReadAutoDialog;
import com.shorts.shortmaker.Services.ForegroundReadSmsService;

import java.util.List;

public class ActionReadAuto implements Action {
    private ReadAutoDialog dialog;
    private int turnOn;


    public ActionReadAuto() {
        this.dialog = new ReadAutoDialog();
    }

    @Override
    public void activate(Application application, Context context, boolean isNewTask) {
        context.startService(new Intent(context, ForegroundReadSmsService.class));
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
