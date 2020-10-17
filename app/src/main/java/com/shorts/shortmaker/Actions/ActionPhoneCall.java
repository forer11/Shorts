package com.shorts.shortmaker.Actions;

import android.Manifest;
import android.app.Activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
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
import com.shorts.shortmaker.ActionDialogs.PhoneCallDialog;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ActionPhoneCall implements Action {
    private static final int REQUEST_CALL = 1;

    private PhoneCallDialog dialog;
    private String number;

    public ActionPhoneCall() {
        this.dialog = new PhoneCallDialog();
    }


    @Override
    public void activate(Application application, final Context context, boolean isNewTask) {
        Log.v("YAY", "Phone call activated");

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
            if (number.trim().length() > 0) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) == PackageManager
                        .PERMISSION_GRANTED) {
                    String dial = "tel:" + number;
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(dial));
                    callIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                    if (isNewTask) {
                        callIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(callIntent);
                }
            }
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,
                            "Please confirm Phone permission",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        number = data.get(0);
    }
}
