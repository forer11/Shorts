package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.AlarmClockDialog;
import com.shorts.shortmaker.ActionDialogs.ChangeOrientationDialog;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ActionChangeOrientation implements Action{
    public static final int PORTRAIT_ORIENTATION = 0;
    public static final int LANDSCAPE_ORIENTATION = 1;
    private ChangeOrientationDialog dialog;
    private int desiredOrientation;


    public ActionChangeOrientation() {
        this.dialog = new ChangeOrientationDialog();
    }
    @Override
    public void activate(Context context, Context activity, boolean isNewTask) {
//        switch (desiredOrientation){
//            case PORTRAIT_ORIENTATION:
//                activity.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
//
//                break;
//            case LANDSCAPE_ORIENTATION:
//                activity.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
//                break;
//        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        desiredOrientation = Integer.parseInt(data.get(0));
    }
}
