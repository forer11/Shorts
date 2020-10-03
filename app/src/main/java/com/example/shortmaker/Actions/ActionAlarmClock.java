package com.example.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.AlarmClockDialog;

import java.util.List;

public class ActionAlarmClock implements Action {

    private AlarmClockDialog dialog;

    public ActionAlarmClock() {
        this.dialog = new AlarmClockDialog();
    }

    @Override
    public void activate(Context context , Activity activity) {
        Log.v("YAY", "Alarm clock activated");
        Toast.makeText(context, "Alarm clock activated", Toast.LENGTH_SHORT).show();
        //TODO - we can decide a constant hour to set the alarm clock to
//        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
//                .putExtra(AlarmClock.EXTRA_HOUR, 10) //hours in 24 hours format
//                .putExtra(AlarmClock.EXTRA_MINUTES, 20)
//                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
//        context.startActivity(intent);

    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }


    @Override
    public void setData(List<String> data) {

    }

}
