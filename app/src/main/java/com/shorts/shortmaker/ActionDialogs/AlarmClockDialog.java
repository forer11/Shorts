package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;


import java.util.ArrayList;

public class AlarmClockDialog extends ActionDialog {

    private Button okButton;
    private TimePicker timePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.alarm_clock_dialog, null);

        initializeDialogViews(builder, view);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        timePicker = view.findViewById(R.id.timePicker);
        okButton = view.findViewById(R.id.okButton);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                okButton.setEnabled(true);
            }
        });
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.alarm_clock_gif);
        buildDialog(builder, view, "Set alarm clock", okButton);
    }


    protected void getUserInput() {
        String desiredHour = Integer.toString(timePicker.getHour());
        String desiredMinutes = Integer.toString(timePicker.getMinute());
        ArrayList<String> results = new ArrayList<>();
        results.add(desiredHour);
        results.add(desiredMinutes);
        String description = "Alarm is set for "
                + addZero(desiredHour) + ":"
                + addZero(desiredMinutes);
        listener.applyUserInfo(results, description);
    }

    private String addZero(String timeUnit) {
        return timeUnit.length() == 1 ? "0" + timeUnit : timeUnit;
    }

}
