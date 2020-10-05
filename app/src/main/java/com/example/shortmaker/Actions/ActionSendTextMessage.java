package com.example.shortmaker.Actions;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.TextMessageDialog;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

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
        // we know we asked for only 1 permission, so we will surely get exactly 1 result
        // (grantResults.size == 1)
        // depending on your use case, if you get only SOME of your permissions
        // (but not all of them), you can act accordingly

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendSms(); // cool
        } else {
            // the user has denied our request! =-O

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.SEND_SMS)) {
                // reached here? means we asked the user for this permission more than once,
                // and they still refuse. This would be a good time to open up a dialog
                // explaining why we need this permission
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

//        Intent sendIntent = new Intent();
//        if (sendThroughWhatsapp) {
//            sendIntent.setPackage(WHATSAPP_PACKAGE_NAME);
//        }
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
//        sendIntent.setType("text/plain");
//        activity.startActivity(sendIntent);



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
