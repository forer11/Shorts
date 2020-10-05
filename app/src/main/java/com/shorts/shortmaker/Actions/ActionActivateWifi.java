package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.WifiDialog;

import java.util.List;

public class ActionActivateWifi implements Action {


    private WifiDialog dialog;

    public ActionActivateWifi() {
        this.dialog = new WifiDialog();
    }


    @Override
    public void activate(Context context , Activity activity) {
        Log.v("YAY", "Wifi activated");
        Toast.makeText(context, "Wifi activated", Toast.LENGTH_SHORT).show();

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {  //TODO else?
            wifi.setWifiEnabled(true);
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {

    }
}
