package com.shorts.shortmaker.Actions;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
    public void activate(final Context context, final Context activity, boolean isNewTask) {
        Log.v("YAY", "Phone call activated");

        Dexter.withContext(activity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void
                    onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (number.trim().length() > 0) {
                            if (ContextCompat.checkSelfPermission(context,
                                    Manifest.permission.CALL_PHONE) == PackageManager
                                    .PERMISSION_GRANTED) {
                                String dial = "tel:" + number;
                                Intent callIntent = new Intent(Intent.ACTION_CALL,
                                        Uri.parse(dial));
                                if (isNewTask) {
                                    callIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                }
                                activity.startActivity(callIntent);
                            }
                        }
                    }

                    @Override
                    public void
                    onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(context, "Enter Phone Number", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void
                    onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest,
                                                       PermissionToken permissionToken) {
                        Toast.makeText(context, "Enter Phone Number",
                                Toast.LENGTH_SHORT).show();
                        // TODO - maybe to a dialog here(can be added using the dexter library automatically!!!)
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
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
