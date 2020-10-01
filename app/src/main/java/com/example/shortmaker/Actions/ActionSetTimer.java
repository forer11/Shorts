package com.example.shortmaker.Actions;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.AlarmClockDialog;
import com.example.shortmaker.ActionDialogs.SetTimerDialog;
import com.example.shortmaker.ActionDialogs.TextMessageDialog;
import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.R;

import java.util.List;

public class ActionSetTimer implements Action {


    private SetTimerDialog dialog;

    public String getTitle() {
        return ActionFactory.TIMER_ACTION_TITLE;
    }

    public ActionSetTimer() {
        this.dialog = new SetTimerDialog();
    }


    @Override
    public void activate(Context context) {
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
}
