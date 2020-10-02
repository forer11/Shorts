package com.example.shortmaker.Actions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.SetTimerDialog;

import java.util.List;

public class ActionSetTimer implements Action {


    private SetTimerDialog dialog;

    public ActionSetTimer() {
        this.dialog = new SetTimerDialog();
    }


    @Override
    public void activate(Context context) {
        Log.v("YAY", "Timer activated");
        Toast.makeText(context, "Timer activated", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
//                .putExtra(AlarmClock.EXTRA_MESSAGE, "message")
//                .putExtra(AlarmClock.EXTRA_LENGTH, 20)
//                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
//        if (intent.resolveActivity(context.getPackageManager()) != null) {
//            context.startActivity(intent);
//        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {

    }
}
