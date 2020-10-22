package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.ReadAutoDialog;
import com.shorts.shortmaker.Services.ForegroundReadSmsService;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.shorts.shortmaker.AppData.inBackground;

public class ActionReadAuto implements Action {
    private ReadAutoDialog dialog;
    private int turnOn;


    public ActionReadAuto() {
        this.dialog = new ReadAutoDialog();
    }

    @Override
    public void activate(Application application, Context context) {
        Intent serviceIntent = new Intent(context, ForegroundReadSmsService.class);
        serviceIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        if (inBackground) {
            serviceIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.startForegroundService(serviceIntent);
        } else {
            application.startService(serviceIntent);
        }
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
