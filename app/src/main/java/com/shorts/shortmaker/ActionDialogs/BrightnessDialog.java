package com.shorts.shortmaker.ActionDialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class BrightnessDialog extends ActionDialog {
    private int brightness;
    private View dialogView;
    private boolean showRealtimeBrightness;
    private int originalBrightness;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (checkForPermission(false)) {
            getOriginalBrightness();
        }


        seekBarListener();
        checkBoxListener();

    }

    private void getOriginalBrightness() {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            ContentResolver cResolver = getContext()
                    .getApplicationContext().getContentResolver();
            try {
                originalBrightness = Settings.System.getInt(
                        cResolver,
                        Settings.System.SCREEN_BRIGHTNESS);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkBoxListener() {
        CheckBox checkBox = dialogView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showRealtimeBrightness = isChecked;
            }
        });
    }

    private void seekBarListener() {
        SeekBar seekBar = dialogView.findViewById(R.id.seek_bar);
        seekBar.setMax(255);
        seekBar.setMin(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
                if (checkForPermission(false)) {
                    if (showRealtimeBrightness) {
                        setBrightness(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setBrightness(int progress) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            ContentResolver cResolver = getContext()
                    .getApplicationContext().getContentResolver();
            Settings.System.putInt(cResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(cResolver,
                    Settings.System.SCREEN_BRIGHTNESS, progress);
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private boolean checkForPermission(boolean isOnDismiss) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Settings.System.canWrite(getContext())) {
                return true;
            } else {
                if (!isOnDismiss) {
                    Intent brightnessIntent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    getContext().startActivity(brightnessIntent);
                    Toast.makeText(getContext(), "Enable Modify system settings and try again"
                            , Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

        }
        return false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        dialogView = layoutInflater.inflate(R.layout.brightness_dialog, null);

        initializeDialogViews(builder, dialogView);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.dark_bright);
        buildDialog(builder, view);
    }

    protected void buildDialog(AlertDialog.Builder builder, View view) {
        builder.setView(view)
                .setTitle("Turn on wifi")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> results = new ArrayList<>();
                        results.add(String.valueOf(brightness));
                        long percentage = Math.round((double) brightness / 255 * 100);
                        String description = "Set brightness to " + percentage + "%";
                        listener.applyUserInfo(results, description);
                    }
                });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (checkForPermission(true)) {
            setBrightness(originalBrightness);
        }
    }
}
