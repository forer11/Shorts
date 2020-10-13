package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.GpsDialog;
import com.shorts.shortmaker.GpsUtils;

import java.util.List;


public class ActionGps implements Action {

    private GpsDialog dialog;
    private int turnOn;
    public static boolean isGPS;

    public static void setIsGPS(boolean isGPS) {
        ActionGps.isGPS = isGPS;
    }


    public ActionGps() {
        this.dialog = new GpsDialog();
    }

    @Override
    public void activate(Context context, final Activity activity) {
        new GpsUtils(context,activity).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        turnOn = Integer.parseInt(data.get(0));
    }


}