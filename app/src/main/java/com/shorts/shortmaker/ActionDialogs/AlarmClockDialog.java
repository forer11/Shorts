package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class AlarmClockDialog extends ActionDialog{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.alarm_clock_dialog,null);

        final TimePicker timePicker = view.findViewById(R.id.timePicker);

        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.alarm_clock_gif).into(imageView);

        builder.setView(view)
                .setTitle("Set alarm clock")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String desiredHour = Integer.toString(timePicker.getHour());
                        String desiredMinutes =Integer.toString(timePicker.getMinute());
                        ArrayList<String> results = new ArrayList<>();
                        results.add(desiredHour);
                        results.add(desiredMinutes);
                        listener.applyUserInfo(results);
                    }
                });


        return builder.create();
    }
}
