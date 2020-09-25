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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ActionDialog.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }
}


