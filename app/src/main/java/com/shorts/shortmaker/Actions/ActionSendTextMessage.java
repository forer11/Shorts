package com.shorts.shortmaker.Actions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.TextMessageDialog;

import java.util.List;


public class ActionSendTextMessage implements Action, ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST_CODE_PERMISSION_SMS = 1546;

    private TextMessageDialog dialog;
    private Activity activity;
    private String whoToSend;
    private String message;

    public ActionSendTextMessage() {
        this.dialog = new TextMessageDialog();
    }


    @Override
    public void activate(Context context, Activity activity) {
        Log.v("YAY", "Send Text activated");
        this.activity = activity;
        boolean hasSmsPermission =
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED;
        if (hasSmsPermission) {
            sendSms();
        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_PERMISSION_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendSms();
        } else {
            Toast.makeText(activity, "Please confirm permission", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.SEND_SMS)) {
                Toast.makeText(activity, "Please confirm permission", Toast.LENGTH_SHORT).show();
                // TODO - maybe to a dialog here
            }
        }


    }

    private void sendSms() {
        SmsManager.getDefault().sendTextMessage(
                whoToSend,
                null,
                message,
                null,
                null);
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
