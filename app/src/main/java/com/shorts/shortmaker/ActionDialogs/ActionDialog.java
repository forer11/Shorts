package com.shorts.shortmaker.ActionDialogs;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import org.jetbrains.annotations.NotNull;

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

    protected CircularProgressDrawable setCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getActivity());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
}


