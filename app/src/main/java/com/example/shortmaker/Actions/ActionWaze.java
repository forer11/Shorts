package com.example.shortmaker.Actions;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.WazeDialog;

import java.util.List;

public class ActionWaze implements Action {

    private String address;
    private WazeDialog dialog;


    public ActionWaze() {
        this.dialog = new WazeDialog();
    }

    @Override
    public void activate(Context context, Activity activity) {
        Log.v("YAY", "Waze activated");
        try {
            openOrInstallWaze("https://waze.com/ul?q=" + address+"&navigate=yes",activity);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            openOrInstallWaze("market://details?id=com.waze",activity);
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        address = data.get(0);
    }

    private void openOrInstallWaze(String uri,Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        activity.startActivity(intent);

    }
}
