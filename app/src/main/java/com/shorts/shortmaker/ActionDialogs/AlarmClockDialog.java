package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.shorts.shortmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlarmClockDialog extends ActionDialog{

    private Button okButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.alarm_clock_dialog,null);

        initializeDialogViews(builder, view);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        final TimePicker timePicker = view.findViewById(R.id.timePicker);
        okButton = view.findViewById(R.id.okButton);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                okButton.setEnabled(true);
            }
        });
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.alarm_clock_gif);
        buildDialog(builder, view, timePicker);
    }

    protected void buildDialog(AlertDialog.Builder builder, View view, final TimePicker timePicker) {
        builder.setView(view)
                .setTitle("Set alarm clock");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInput(timePicker);
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

    protected void getUserInput(TimePicker timePicker) {
        String desiredHour = Integer.toString(timePicker.getHour());
        String desiredMinutes =Integer.toString(timePicker.getMinute());
        ArrayList<String> results = new ArrayList<>();
        results.add(desiredHour);
        results.add(desiredMinutes);
        listener.applyUserInfo(results);
    }

}
