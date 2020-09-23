package com.example.shortmaker.Actions;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.example.shortmaker.DataClasses.Action;

public class ActionAlarmClock implements Action {

    private Context context;

    public ActionAlarmClock(Context context) {
        this.context = context;
    }

    @Override
    public void activate() {
        //TODO - we can decide a constant hour to set the alarm clock to
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR,10) //hours in 24 hours format
                .putExtra(AlarmClock.EXTRA_MINUTES,20);
        context.startActivity(intent);
    }

    @Override
    public void setData(String stringData, Integer intData) {

    }


}
