package com.shorts.shortmaker.Actions;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.TextMessageDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActionSendTextMessage implements Action {

    private static final int REQUEST_CODE_PERMISSION_SMS = 1546;
    private static Map<String, String> reverseContacts = new HashMap<>();
    private TextMessageDialog dialog;
    private Context activity;
    private String whoToSend;
    private String message;
    private List<String> recipientsList;

    public ActionSendTextMessage() {
        this.dialog = new TextMessageDialog();
    }


    @Override
    public void activate(Application application, final Context context, boolean isNewTask) {
        this.activity = context;

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {
            sendSms();
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,
                            "Please confirm SMS permission",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void sendSms() {
        for (int i = 0; i < recipientsList.size(); i++) {
            SmsManager.getDefault().sendTextMessage(
                    recipientsList.get(i),
                    null,
                    message,
                    null,
                    null);
        }
    }


    @Override
    public ActionDialog getDialog() {
        return dialog;
    }


    @Override
    public void setData(List<String> data) {
        message = data.get(0);
        recipientsList = data.subList(1, data.size());
    }
}
