package com.example.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.SetTimerDialog;

import java.util.List;

public class ActionSetTimer implements Action {


    private SetTimerDialog dialog;

    private int hour;
    private int minutes;
    private int seconds;

    public ActionSetTimer() {
        this.dialog = new SetTimerDialog();
    }


    @Override
    public void activate(Context context, Activity activity) {
        Log.v("YAY", "Timer activated");
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        hour = Integer.parseInt(data.get(0))*3600;
        minutes = Integer.parseInt(data.get(1))*60;
        seconds = Integer.parseInt(data.get(2))+hour+minutes;
        System.out.println("");
    }
}
