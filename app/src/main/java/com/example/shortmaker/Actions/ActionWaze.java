package com.example.shortmaker.Actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.shortmaker.DataClasses.Action;

public class ActionWaze implements Action {

    private Context context;
    
    public ActionWaze(Context context) {
        this.context = context;
    }


    @Override
    public void activate() {
        try {
            // Launch Waze to look for Hawaii:.
            // we can also give the following uri :  "https://waze.com/ul?ll=40.761043,-73.980545&navigate=yes" for it to navigate automatically
          //TODO - change to user defined address
            openOrInstallWaze("https://waze.com/ul?q=Hawaii");
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            openOrInstallWaze("market://details?id=com.waze");
        }
    }

    @Override
    public void setData(String stringData, Integer intData) {

    }



    private void openOrInstallWaze(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }
}
