package com.example.shortmaker.ActionDialogs;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

abstract public class ActionDialog extends AppCompatDialogFragment {
    protected DialogListener listener;
    protected OnClickListener onClickListener;


    public interface DialogListener {
        void applyUserInfo(ArrayList<String> data);
    }

    public void setNewDialogListener(DialogListener listener){
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setNewOnClickListener(OnClickListener listener){
        this.onClickListener = listener;
    }
}


