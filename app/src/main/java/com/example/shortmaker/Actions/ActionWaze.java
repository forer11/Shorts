package com.example.shortmaker.Actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.WazeDialog;
import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.R;


import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ActionWaze implements Action {

    private String address;
    private WazeDialog dialog;
    private ArrayList<String> data;


    public ActionWaze() {
        this.dialog = new WazeDialog();
        this.data = new ArrayList<>();
    }

    @Override
    public void activate(Context context) {
        try {
            // Launch Waze to look for Hawaii:.
            // we can also give the following uri :  "https://waze.com/ul?ll=40.761043,-73.980545&navigate=yes" for it to navigate automatically
            //TODO - change to user defined address
            openOrInstallWaze("https://waze.com/ul?q=" + address);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            openOrInstallWaze("market://details?id=com.waze");
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

    @Override
    public String getTitle() {
        return ActionFactory.WAZE_ACTION_TITLE;
    }


    private void openOrInstallWaze(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    }
}
