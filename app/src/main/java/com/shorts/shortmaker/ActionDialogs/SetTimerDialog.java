package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SetTimerDialog extends ActionDialog {

    public static final int HOURS_MAX_VAL = 24;
    public static final int MINUTES_MAX_VAL = 60;
    public static final int SECONDS_MAX_VAL = 60;
    protected String minute = "0";
    protected String hour = "0";
    protected String second = "0";
    private Button okButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.set_timer_dialog, null);

        initializeDialogViews(builder, view);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.timer_gif);
        okButton = view.findViewById(R.id.okButton);
        setPickers(view);
        buildDialog(builder, view, "Set timer", okButton);
    }

    protected void setPickers(View view) {
        String[] hours = getPickerArray(HOURS_MAX_VAL);
        String[] minutes = getPickerArray(MINUTES_MAX_VAL);
        String[] seconds = getPickerArray(MINUTES_MAX_VAL);

        NumberPicker hoursPicker = (NumberPicker) view.findViewById(R.id.hoursPicker);
        NumberPicker minutesPicker = (NumberPicker) view.findViewById(R.id.minutesPicker);
        NumberPicker secondsPicker = (NumberPicker) view.findViewById(R.id.secondsPicker);

        setPicker(hours, hoursPicker, HOURS_MAX_VAL - 1, "Hours");
        setPicker(minutes, minutesPicker, MINUTES_MAX_VAL - 1, "Minutes");
        setPicker(seconds, secondsPicker, SECONDS_MAX_VAL - 1, "Seconds");
    }


    @NotNull
    protected String[] getPickerArray(int range) {
        String[] numbers = new String[range];

        for (int i = 0; i < range; i++) {
            numbers[i] = String.valueOf(i);
        }
        return numbers;
    }

    protected void setPicker(String[] pickerValues, NumberPicker picker, int maxValue,
                             final String id) {
        picker.setMinValue(0);
        picker.setMaxValue(maxValue);
        picker.setDisplayedValues(pickerValues);
        setPickerOnValueChangedListener(picker, id);
    }

    private void setPickerOnValueChangedListener(NumberPicker picker, final String id) {
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (id) {
                    case "Hours":
                        hour = Integer.toString(picker.getValue());
                        break;
                    case "Minutes":
                        minute = Integer.toString(picker.getValue());
                        break;
                    case "Seconds":
                        second = Integer.toString(picker.getValue());
                        break;
                }
                setEnableOkButton();
            }
        });
    }

    protected void setEnableOkButton() {
        if (!(hour.equals("0") && minute.equals("0") && second.equals("0"))) {
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(false);
        }
    }

    protected void getUserInput() {
        if (hour.equals("0") && minute.equals("0") && second.equals("0")) {
            Toast.makeText(getContext(), "Please insert desired time",
                    Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> results = new ArrayList<>();
            results.add(hour);
            results.add(minute);
            results.add(second);
            String description = "Set Timer for "
                    + addZero(hour) + ":"
                    + addZero(minute) + ":"
                    + addZero(second);
            listener.applyUserInfo(results, description);
        }
    }

    private String addZero(String timeUnit) {
        return timeUnit.length() == 1 ? "0" + timeUnit : timeUnit;
    }
}
