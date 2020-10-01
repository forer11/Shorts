package com.example.shortmaker.ActionDialogs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

abstract public class ActionDialog extends AppCompatDialogFragment {
    protected DialogListener listener;


    public interface DialogListener {
        void applyUserInfo(ArrayList<String> data);
    }

    public void setNewDialogListener(DialogListener listener){
        this.listener = listener;
    }
}


