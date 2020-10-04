package com.example.shortmaker.Actions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.PhoneCallDialog;

import java.util.List;

public class ActionPhoneCall implements Action, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_CALL = 1;
    private PhoneCallDialog dialog;
    private String number;
    private Activity activity;
    private Context context;

    public ActionPhoneCall() {
        this.dialog = new PhoneCallDialog();
    }

    private void makePhoneCall(Context context, Activity activity,String number) {
        this.activity=activity;
        this.context=context;
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(context, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void activate(Context context , Activity activity) {
        Log.v("YAY", "Phone call activated");

        makePhoneCall(context, activity,number);
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        String prefix = data.get(0);
        number = prefix.concat(data.get(1));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(context, activity,number);
            } else {
                Toast.makeText(context, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
