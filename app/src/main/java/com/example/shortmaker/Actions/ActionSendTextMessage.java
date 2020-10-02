package com.example.shortmaker.Actions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.TextMessageDialog;

import java.util.List;

public class ActionSendTextMessage implements Action {

    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    private TextMessageDialog dialog;

    private boolean sendThroughWhatsapp;

    public ActionSendTextMessage() {
        this.dialog = new TextMessageDialog();
    }


    @Override
    public void activate(Context context) {
        Log.v("YAY", "Send Text activated");
        Toast.makeText(context, "Send Text activated", Toast.LENGTH_SHORT).show();

//        Intent sendIntent = new Intent();
//        if (sendThroughWhatsapp) {
//            sendIntent.setPackage(WHATSAPP_PACKAGE_NAME);
//        }
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send."); //TODO - change to a costumized user text
//        sendIntent.setType("text/plain");
//        context.startActivity(sendIntent);
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }


    @Override
    public void setData(List<String> data) {

    }
}
