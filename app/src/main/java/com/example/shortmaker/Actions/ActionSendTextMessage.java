package com.example.shortmaker.Actions;

import android.content.Context;
import android.content.Intent;

import com.example.shortmaker.DataClasses.Action;

public class ActionSendTextMessage implements Action {

    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    private Context context;
    private boolean sendThroughWhatsapp;

    public ActionSendTextMessage(Context context,boolean sendThroughWhatsapp) {
        this.context = context;
        this.sendThroughWhatsapp = sendThroughWhatsapp;
    }


    @Override
    public void activate() {
        Intent sendIntent = new Intent();
        if (sendThroughWhatsapp) {
            sendIntent.setPackage(WHATSAPP_PACKAGE_NAME);
        }
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send."); //TODO - change to a costumized user text
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    @Override
    public void setData(String stringData, Integer intData) {

    }
}
