package com.example.shortmaker.Actions;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;

import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.ActionDialog;

import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ActionSoundSettings implements Action, Searchable {

    public static final int SILENT_MODE = 0;
    public static final int VIBRATE_MODE = 1;
    public static final int RING_MODE = 2;
    private Context context;
    private int mode;

    public ActionSoundSettings(Context context, int mode) {
        this.context = context;
        this.mode = mode;
    }


    @Override
    public void activate() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        switch (mode) {
            case SILENT_MODE:
                putPhoneOnSilent(audioManager);
                break;
            case VIBRATE_MODE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            case RING_MODE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
        }
    }

    @Override
    public ActionDialog getDialog() {
        return null;
    }

    @Override
    public void setData(List<String> data) {

    }

    private void putPhoneOnSilent(AudioManager audioManager) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            context.startActivity(intent);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    @Override
    public String getTitle() {
        switch (mode) {
            case SILENT_MODE:
                return "Silent mode action";
            case VIBRATE_MODE:
                return "Vibrate mode action";
            case RING_MODE:
                return "Ring mode action";
        }
        return "";
    }
}
