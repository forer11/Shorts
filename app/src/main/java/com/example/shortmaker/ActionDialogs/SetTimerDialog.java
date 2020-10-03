package com.example.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.shortmaker.R;
import com.google.type.TimeOfDay;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

import static android.media.CamcorderProfile.get;

public class SetTimerDialog extends ActionDialog {

    protected String minute;
    protected String hour;
    protected String seconds;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.set_timer_dialog,null);

        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.timer_gif).into(imageView);
        Button setTimerButton = view.findViewById(R.id.setTimer);
        setTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetTimerDialog();
            }
        });
        builder.setView(view)
                .setTitle("Set timer")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(hour.equals("") || minute.equals("") || seconds.equals("")) {
                            Toast.makeText(getContext(), "Please insert desired time", Toast.LENGTH_SHORT).show();
                        }else{
                            ArrayList<String> results = new ArrayList<>();
                            results.add(hour);
                            results.add(minute);
                            results.add(seconds);
                            listener.applyUserInfo(results);
                        }
                    }
                });
        return builder.create();
    }

    @NotNull
    private void showSetTimerDialog() {
        Calendar calendar = Calendar.getInstance();
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(getContext(), new MyTimePickerDialog.OnTimeSetListener() {



            @Override
            public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minuteOfDay, int secondsOfDay) {
                hour = Integer.toString(hourOfDay);
                minute = Integer.toString(minuteOfDay);
                seconds = Integer.toString(secondsOfDay);
            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), true);
        mTimePicker.show();
    }
}
