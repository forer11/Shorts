package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.GoogleMapsDialog;
import com.shorts.shortmaker.ActionDialogs.WazeDialog;

import java.util.List;

public class ActionGoogleMaps implements Action {

    private String address;
    private GoogleMapsDialog dialog;


    public ActionGoogleMaps() {
        this.dialog = new GoogleMapsDialog();
    }

    @Override
    public void activate(Context context, Activity activity) {
        //TODO - add here the location entered
        Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
       activity.startActivity(mapIntent);
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        address = data.get(0);
    }

}
