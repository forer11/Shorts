package com.example.shortmaker.Actions;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;


import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.ActionDialog;

import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ActionPhoneCall implements Action, Searchable {

    private static final int REQUEST_CALL = 1;
    public static final String PHONE_CALL_DIALOG_TITLE = "Make a phone call";
    public static final String PHONE_CALL_DIALOG_POS_BTN = "DIAL";
    private EditText editText;
    private View phoneCallDialogLayout;
    private AlertDialog makeCallDialog;
    private Context context;

    public ActionPhoneCall(Context context) {
        this.context=context;
    }


    @Override
    public void activate() {

    }

    @Override
    public ActionDialog getDialog() {
        return null;
    }

    @Override
    public void setData(List<String> data) {

    }

    @Override
    public String getTitle() {
        return "Make a phone call action";
    }
}
