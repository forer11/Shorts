package com.example.shortmaker.Actions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.AlarmClockDialog;
import com.example.shortmaker.ActionDialogs.PhoneCallDialog;
import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.R;

import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ActionPhoneCall implements Action, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_CALL = 1;
    public static final String PHONE_CALL_DIALOG_TITLE = "Make a phone call";
    public static final String PHONE_CALL_DIALOG_POS_BTN = "DIAL";
    private EditText editText;
    private View phoneCallDialogLayout;
    private AlertDialog makeCallDialog;
    private PhoneCallDialog dialog;

    public String getTitle() {
        return ActionFactory.PHONE_CALL_ACTION_TITLE;
    }

    public ActionPhoneCall() {
        this.dialog = new PhoneCallDialog();
    }

    
    private void makePhoneCall(Context context,String number) {
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(context, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void activate(Context context) {
        makePhoneCall(context,""); //TODO - see how to arrange it
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {

    }

    //TODO - transfer to main
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                makePhoneCall(editText.getText().toString());
            } else {
//                Toast.makeText(context, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
