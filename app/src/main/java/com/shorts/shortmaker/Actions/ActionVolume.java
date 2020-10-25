package com.shorts.shortmaker.Actions;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.BrightnessDialog;
import com.shorts.shortmaker.ActionDialogs.ChangeVolumeDialog;

import java.util.ArrayList;
import java.util.List;

import static com.shorts.shortmaker.ActionDialogs.ChangeVolumeDialog.POSITION_MEDIA;
import static com.shorts.shortmaker.ActionDialogs.ChangeVolumeDialog.POSITION_NOTIFICATIONS;
import static com.shorts.shortmaker.ActionDialogs.ChangeVolumeDialog.POSITION_RING;

public class ActionVolume implements Action {


    private ChangeVolumeDialog dialog;
    ArrayList<Integer> volumes;

    public ActionVolume() {
        this.dialog = new ChangeVolumeDialog();
    }


    @Override
    public void activate(Application application, Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setStreamVolume(AudioManager.STREAM_RING,
                    volumes.get(POSITION_RING),
                    0);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    volumes.get(POSITION_MEDIA),
                    0);
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                    volumes.get(POSITION_NOTIFICATIONS),
                    0);
        } else {
            Toast.makeText(application, "Error with audio manager", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        volumes = new ArrayList<>();
        volumes.add(Integer.parseInt(data.get(POSITION_RING)));
        volumes.add(Integer.parseInt(data.get(POSITION_MEDIA)));
        volumes.add(Integer.parseInt(data.get(POSITION_NOTIFICATIONS)));
    }
}
