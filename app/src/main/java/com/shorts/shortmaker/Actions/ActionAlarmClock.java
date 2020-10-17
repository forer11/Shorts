package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.AlarmClockDialog;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ActionAlarmClock implements Action {

    private AlarmClockDialog dialog;
    private int hour;
    private int minute;

    public ActionAlarmClock() {
        this.dialog = new AlarmClockDialog();
    }

    @Override
    public void activate(Application application, Context context, boolean isNewTask) {
        Log.v("YAY", "Alarm clock activated");
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hour) //hours in 24 hours format
                .putExtra(AlarmClock.EXTRA_MINUTES, minute)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (isNewTask) {
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }


    @Override
    public void setData(List<String> data) {
        hour = Integer.parseInt(data.get(0));
        minute = Integer.parseInt(data.get(1));
    }

}
