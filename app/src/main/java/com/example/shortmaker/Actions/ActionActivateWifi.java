package com.example.shortmaker.Actions;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.AlarmClock;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.SetTimerDialog;
import com.example.shortmaker.ActionDialogs.WifiDialog;
import com.example.shortmaker.R;

import java.util.List;

public class ActionActivateWifi implements Action {


    private Context context;
    private WifiDialog dialog;

    public ActionActivateWifi(Context context) {
        this.context = context;
        this.dialog = new WifiDialog();
    }


    @Override
    public void activate() {
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

    @Override
    public String getTitle() {
        return "Activate wifi action";
    }

    @Override
    public int getImageResource() {
        return R.drawable.wifi_icon;
    }
}
