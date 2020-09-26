package com.example.shortmaker.Actions;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.AlarmClockDialog;
import com.example.shortmaker.ActionDialogs.SpotifyDialog;

import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ActionAlarmClock implements Action, Searchable {

    private Context context;
    private AlarmClockDialog dialog;

    public ActionAlarmClock(Context context) {
        this.context = context;
        this.dialog = new AlarmClockDialog();
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
        return dialog;
    }


    @Override
    public void setData(List<String> data) {

    }

    @Override
    public String getTitle() {
        return "Set alarm clock action";
    }


}
