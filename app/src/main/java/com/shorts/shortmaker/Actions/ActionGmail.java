package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.GmailDialog;
import com.shorts.shortmaker.ActionDialogs.SpotifyDialog;
import com.shorts.shortmaker.GMailSender;

import java.util.List;

public class ActionGmail implements Action {
    private GmailDialog dialog;
    private String userEmail;
    private String userPassword;
    private String recipientEmail;
    private String messageSubject;
    private String messageBody;

    public ActionGmail() {
        this.dialog = new GmailDialog();
    }

    @Override
    public void activate(Application application , Context context, boolean isNewTask) {
        Log.v("YAY", "Gmail activated");

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(userEmail,
                            userPassword);
                    sender.sendMail(messageBody, messageSubject,
                            userEmail, recipientEmail);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        userEmail = data.get(0);
        userPassword = data.get(1);
        recipientEmail = data.get(2);
        messageSubject = data.get(3);
        messageBody = data.get(4);

    }



}
