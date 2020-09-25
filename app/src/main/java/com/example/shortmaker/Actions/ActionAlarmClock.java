package com.example.shortmaker.Actions;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.ActionDialog;

import java.util.List;

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
                .putExtra(AlarmClock.EXTRA_MINUTES,20)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(intent);

    }

    @Override
    public ActionDialog getDialog() {
        return null;
    }


    @Override
    public void setData(List<String> data) {

    }


}
