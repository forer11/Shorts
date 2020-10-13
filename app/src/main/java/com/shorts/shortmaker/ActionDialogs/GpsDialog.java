package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class GpsDialog extends ActionDialog {
    public static final String ON = "On";
    public static final String OFF = "Off";

    private Button okButton;
    private View view;
    private String state;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.gps_dialog, null);
        initializeDialogViews(view);
        setSwitch();
        buildDialog(builder, view, "Set GPS On/Off", okButton);
        return builder.create();
    }

    protected void initializeDialogViews(View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.gps_gif);
        okButton = view.findViewById(R.id.okButton);
    }


    protected void setSwitch() {
        final TextView onOffTextView = view.findViewById(R.id.enable_disable_textview);
        Switch onOffSwitch = view.findViewById(R.id.enable_disable_switch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    state = ON;
                    onOffTextView.setText("GPS enabled");
                } else {
                    state = OFF;
                    onOffTextView.setText("GPS disabled");
                }
            }
        });
        onOffSwitch.setChecked(true);
    }

    protected void getUserInput() {
        ArrayList<String> results = new ArrayList<>();
        results.add(state);
        String description = "Gps set to: " + state;
        listener.applyUserInfo(results, description);
    }
}

