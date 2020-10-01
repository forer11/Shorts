package com.example.shortmaker.Actions;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;

import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.AlarmClockDialog;
import com.example.shortmaker.ActionDialogs.SoundSettingsDialog;
import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.R;

import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ActionSoundSettings implements Action {

    public static final int SILENT_MODE = 0;
    public static final int VIBRATE_MODE = 1;
    public static final int RING_MODE = 2;
    private int mode;
    private SoundSettingsDialog dialog;

    public String getTitle() {
        return ActionFactory.SOUND_MODE_ACTION_TITLE;
    }


    public ActionSoundSettings() {
        this.dialog = new SoundSettingsDialog();
    }


    @Override
    public void activate(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        switch (mode) {
            case SILENT_MODE:
                putPhoneOnSilent(audioManager,context);
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
        return dialog;
    }

    @Override
    public void setData(List<String> data) {

    }

    private void putPhoneOnSilent(AudioManager audioManager,Context context) {
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
}
