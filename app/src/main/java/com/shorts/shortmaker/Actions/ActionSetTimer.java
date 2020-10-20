package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.SetTimerDialog;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.shorts.shortmaker.AppData.inBackground;

public class ActionSetTimer implements Action {


    private SetTimerDialog dialog;

    private int hour;
    private int minutes;
    private int seconds;

    public ActionSetTimer() {
        this.dialog = new SetTimerDialog();
    }


    @Override
    public void activate(Application application, Context context) {
        Log.v("YAY", "Timer activated");
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (inBackground) {
            intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        }
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        hour = Integer.parseInt(data.get(0)) * 3600;
        minutes = Integer.parseInt(data.get(1)) * 60;
        seconds = Integer.parseInt(data.get(2)) + hour + minutes;
    }
}
