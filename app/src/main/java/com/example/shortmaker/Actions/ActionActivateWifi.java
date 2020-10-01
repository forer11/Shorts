package com.example.shortmaker.Actions;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.WifiDialog;
import com.example.shortmaker.ActionFactory;

import java.util.List;

public class ActionActivateWifi implements Action {


    private WifiDialog dialog;

    public String getTitle() {
        return ActionFactory.WIFI_ACTION_TITLE;
    }
    public ActionActivateWifi() {
        this.dialog = new WifiDialog();
    }


    @Override
    public void activate(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {

    }
}
