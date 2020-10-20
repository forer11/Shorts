package com.shorts.shortmaker.Actions;

import android.app.Application;
import android.content.Context;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.ChangeOrientationDialog;

import java.util.List;

public class ActionChangeOrientation implements Action{
    public static final int PORTRAIT_ORIENTATION = 0;
    public static final int LANDSCAPE_ORIENTATION = 1;
    private ChangeOrientationDialog dialog;
    private int desiredOrientation;


    public ActionChangeOrientation() {
        this.dialog = new ChangeOrientationDialog();
    }
    @Override
    public void activate(Application application, Context context) {
//        switch (desiredOrientation){
//            case PORTRAIT_ORIENTATION:
//                context.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
//
//                break;
//            case LANDSCAPE_ORIENTATION:
//                context.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
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
