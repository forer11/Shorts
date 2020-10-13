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

public class SetBluetoothDialog extends ActionDialog {

    public static final String ON = "On";
    public static final String OFF = "Off";

    private View view;
    private String state;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.bluetooth_dialog, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.blutooth_gif);
        setSwitch();
        Button okButton = view.findViewById(R.id.okButton);
        buildDialog(builder, view, "Set Bluetooth On/Off", okButton);
        return builder.create();
    }

    protected void setSwitch() {
        final TextView onOffTextView = view.findViewById(R.id.enable_disable_textview);
        Switch onOffSwitch = view.findViewById(R.id.enable_disable_switch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    state = ON;
                    onOffTextView.setText("Bluetooth enabled");
                } else {
                    state = OFF;
                    onOffTextView.setText("Bluetooth disabled");
                }
            }
        });
        onOffSwitch.setChecked(true);
    }

    @Override
    protected void getUserInput() {
        super.getUserInput();
        ArrayList<String> data = new ArrayList<String>();
        data.add(state);
        String description = "Bluetooth set to: " + state;
        listener.applyUserInfo(data, description);
    }
}
