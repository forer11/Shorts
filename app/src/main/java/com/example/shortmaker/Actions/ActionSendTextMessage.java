package com.example.shortmaker.Actions;

import android.content.Context;
import android.content.Intent;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.TextMessageDialog;
import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.R;

import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ActionSendTextMessage implements Action {

    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    private TextMessageDialog dialog;
    private boolean sendThroughWhatsapp;

    public String getTitle() {
        return ActionFactory.TEXT_MESSAGE_ACTION_TITLE;
    }

    public ActionSendTextMessage() {
        this.dialog = new TextMessageDialog();
    }


    @Override
    public void activate(Context context) {
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
    public ActionDialog getDialog() {
        return dialog;
    }


    @Override
    public void setData(List<String> data) {

    }
}
