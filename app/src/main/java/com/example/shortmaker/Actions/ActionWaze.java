package com.example.shortmaker.Actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.WazeDialog;
import com.example.shortmaker.DataClasses.Action;


import java.util.ArrayList;
import java.util.List;

public class ActionWaze implements Action {

    private Context context;
    private String address;
    private WazeDialog dialog;
    private ArrayList<String> data;


    public ActionWaze(Context context) {
        this.context = context;
        this.dialog = new WazeDialog();
        this.data = new ArrayList<>();
    }

    public DialogFragment getDialog() {
        return dialog;
    }

    @Override
    public void activate() {
        try {
            // Launch Waze to look for Hawaii:.
            // we can also give the following uri :  "https://waze.com/ul?ll=40.761043,-73.980545&navigate=yes" for it to navigate automatically
            //TODO - change to user defined address
            openOrInstallWaze("https://waze.com/ul?ll=40.761043,-73.980545&navigate=yes");
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            openOrInstallWaze("market://details?id=com.waze");
        }
    }

    @Override
    public void setData(List<String> params) {
        address = params.get(0);
        activate();
    }


    private void openOrInstallWaze(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

}
