package com.example.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.AlarmClockDialog;

import java.util.List;

public class ActionAlarmClock implements Action {

    private AlarmClockDialog dialog;
    private int hour;
    private int minute;

    public ActionAlarmClock() {
        this.dialog = new AlarmClockDialog();
    }

    @Override
    public void activate(Context context , Activity activity) {
        Log.v("YAY", "Alarm clock activated");
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hour) //hours in 24 hours format
                .putExtra(AlarmClock.EXTRA_MINUTES, minute)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        activity.startActivity(intent);

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
