package com.example.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.TextMessageDialog;

import java.util.List;

public class ActionSendTextMessage implements Action {

    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    private TextMessageDialog dialog;

    private boolean sendThroughWhatsapp;
    private String whoToSend;
    private String message;

    public ActionSendTextMessage() {
        this.dialog = new TextMessageDialog();
    }


    @Override
    public void activate(Context context , Activity activity) {
        Log.v("YAY", "Send Text activated");

        Intent sendIntent = new Intent();
        if (sendThroughWhatsapp) {
            sendIntent.setPackage(WHATSAPP_PACKAGE_NAME);
        }
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }


    @Override
    public void setData(List<String> data) {
        whoToSend = data.get(0);
        message = data.get(1);
    }
}
