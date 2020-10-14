package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.WazeDialog;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ActionWaze implements Action {

    private WazeDialog dialog;
    private String latitude;
    private String longtitude;


    public ActionWaze() {
        this.dialog = new WazeDialog();
    }

    @Override
    public void activate(Context context, Context activity, boolean isNewTask) {
        Log.v("YAY", "Waze activated");
        try {
            openOrInstallWaze("https://waze.com/ul?q=" + latitude + "," + longtitude + "&navigate=yes",
                    activity,
                    isNewTask);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            openOrInstallWaze("market://details?id=com.waze", activity, isNewTask);
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        latitude = data.get(0);
        longtitude = data.get(1);
    }

    private void openOrInstallWaze(String uri, Context activity, boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        if (isNewTask) {
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }
}
