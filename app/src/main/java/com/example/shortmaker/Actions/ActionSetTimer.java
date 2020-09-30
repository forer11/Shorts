package com.example.shortmaker.Actions;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.AlarmClockDialog;
import com.example.shortmaker.ActionDialogs.SetTimerDialog;
import com.example.shortmaker.ActionDialogs.TextMessageDialog;
import com.example.shortmaker.R;

import java.util.List;

public class ActionSetTimer implements Action {


    private Context context;
    private SetTimerDialog dialog;

    public ActionSetTimer(Context context) {
        this.context = context;
        this.dialog = new SetTimerDialog();
    }


    @Override
    public void activate() {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "message")
                .putExtra(AlarmClock.EXTRA_LENGTH, 20)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
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

    }

    @Override
    public String getTitle() {
        return "Set timer action";
    }

    @Override
    public int getImageResource() {
        return R.drawable.timer_icon;
    }
}
