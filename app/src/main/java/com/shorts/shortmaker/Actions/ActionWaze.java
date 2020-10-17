package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
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
    private String addressFormat;
    private String latitude;
    private String longtitude;
    private String name;


    public ActionWaze() {
        this.dialog = new WazeDialog();
    }

    @Override
    public void activate(Application application, Context context, boolean isNewTask) {
        Log.v("YAY", "Waze activated");
        try {
            openOrInstallWaze("https://waze.com/ul?q=" + name + "%20" + addressFormat
                            + "&ll=" + latitude + "," + longtitude +
                            "&navigate=yes",
                    context,
                    isNewTask);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            openOrInstallWaze("market://details?id=com.waze", context, isNewTask);
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        addressFormat = data.get(0);
        latitude = data.get(1);
        longtitude = data.get(2);
        name = data.get(3);
    }

    private void openOrInstallWaze(String uri, Context activity, boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        if (isNewTask) {
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }
}
