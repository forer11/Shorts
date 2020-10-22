package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.GoogleMapsDialog;
import com.shorts.shortmaker.ActionDialogs.WazeDialog;

import java.util.HashMap;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.shorts.shortmaker.AppData.inBackground;

public class ActionGoogleMaps implements Action {

    private String address;
    private GoogleMapsDialog dialog;
    private HashMap<String, String> transportationModes = new HashMap<String, String>() {{
        put("Driving", "d");
        put("Bicycling", "b");
        put("wheeler", "l");
        put("Walking", "w");
    }};
    private String latitude;
    private String longtitude;
    private String transportationShortcutName;

    public ActionGoogleMaps() {
        this.dialog = new GoogleMapsDialog();
    }

    @Override
    public void activate(Application application, Context context) {
        //TODO - add here the location entered
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude
                + "," + longtitude + "&mode=" + transportationShortcutName);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        if (inBackground) {
            mapIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        }
        mapIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        latitude = data.get(0);
        longtitude = data.get(1);
        String transportaionWay = data.get(2);
        transportationShortcutName = transportationModes.get(transportaionWay);
    }

}
