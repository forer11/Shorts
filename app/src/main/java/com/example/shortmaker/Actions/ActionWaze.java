package com.example.shortmaker.Actions;

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
    public void activate(Context context) {
        Log.v("YAY", "Waze activated");
        Toast.makeText(context, "Waze activated" + address, Toast.LENGTH_SHORT).show();
//        try {
//            // Launch Waze to look for Hawaii:.
//            // we can also give the following uri :  "https://waze.com/ul?ll=40.761043,-73.980545&navigate=yes" for it to navigate automatically
//            //TODO - change to user defined address
//            openOrInstallWaze("https://waze.com/ul?q=" + address);
//        } catch (ActivityNotFoundException ex) {
//            // If Waze is not installed, open it in Google Play:
//            openOrInstallWaze("market://details?id=com.waze");
//        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        address = data.get(0);
    }

    private void openOrInstallWaze(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    }
}
