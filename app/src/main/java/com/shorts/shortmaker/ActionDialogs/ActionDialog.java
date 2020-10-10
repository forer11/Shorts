package com.shorts.shortmaker.ActionDialogs;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.shorts.shortmaker.R;


import java.util.ArrayList;

abstract public class ActionDialog extends AppCompatDialogFragment {
    protected DialogListener listener;
    protected OnClickListener onClickListener;


    public interface DialogListener {
        void applyUserInfo(ArrayList<String> data, String description);
    }

    public void setNewDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setNewOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    private CircularProgressDrawable setCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new
                CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    protected void setDialogImage(ImageView imageView, int imagePath) {
        CircularProgressDrawable circularProgressDrawable = setCircularProgressBar();
        //Glide.with(this).load(R.drawable.alarm_clock_gif).placeholder(circularProgressDrawable).into(imageView);
        Glide.with(this).load(imagePath)
                .transition(withCrossFade())
                .placeholder(circularProgressDrawable)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imageView);
    }

    protected void getUserInput() {

    }

    protected void buildDialog(AlertDialog.Builder builder, View view, String dialogTitle,
                               Button okButton) {
        builder.setView(view)
                .setTitle(dialogTitle);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInput();
                dismiss();
            }
        });
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}


