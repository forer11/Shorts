package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class ChangeVolumeDialog extends ActionDialog {
    public static final int POSITION_RING = 0;
    public static final int POSITION_MEDIA = 1;
    public static final int POSITION_NOTIFICATIONS = 2;
    private View dialogView;
    private Button okButton;
    private ArrayList<Integer> volumeList = new ArrayList<Integer>();
    private ArrayList<MultiSeekBar> seekBars = new ArrayList<>();
    private AudioManager audioManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getContext() != null) {
            audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            volumeList.add(audioManager.getStreamVolume(AudioManager.STREAM_RING));
            volumeList.add(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeList.add(audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));

            setSeekBars();


        } else {
            dismiss();
        }

    }

    private void setSeekBars() {
        MultiSeekBar seekRing = new MultiSeekBar(dialogView.findViewById(R.id.ring_seek_bar),
                POSITION_RING,
                AudioManager.STREAM_RING,
                R.mipmap.volume_off_icon,
                R.mipmap.volume_icon,
                dialogView.findViewById(R.id.ring_icon));
        seekRing.seekBarListener();
        seekBars.add(seekRing);

        MultiSeekBar seekMedia = new MultiSeekBar(dialogView.findViewById(R.id.media_seek_bar),
                POSITION_MEDIA,
                AudioManager.STREAM_MUSIC,
                R.mipmap.media_off,
                R.mipmap.media_on,
                dialogView.findViewById(R.id.media_icon));
        seekMedia.seekBarListener();
        seekBars.add(seekMedia);

        MultiSeekBar seekNotifications =
                new MultiSeekBar(dialogView.findViewById(R.id.notification_seek_bar),
                        POSITION_NOTIFICATIONS,
                        AudioManager.STREAM_NOTIFICATION,
                        R.mipmap.notification_off,
                        R.mipmap.notification_on,
                        dialogView.findViewById(R.id.notification_icon));
        seekNotifications.seekBarListener();
        seekBars.add(seekNotifications);
    }

    private class MultiSeekBar {
        private SeekBar seekBar;
        private int position;
        private int streamMode;
        private int imageEmpty;
        private int imageFull;
        private ImageView imageView;

        MultiSeekBar(SeekBar seekBar,
                     int position,
                     int streamMode,
                     int imageEmpty,
                     int imageFull,
                     ImageView imageView) {
            this.seekBar = seekBar;
            this.position = position;
            this.streamMode = streamMode;
            this.imageEmpty = imageEmpty;
            this.imageFull = imageFull;
            this.imageView = imageView;
        }

        void seekBarListener() {
            int maxVolume = audioManager.getStreamMaxVolume(streamMode);
            seekBar.setMax(maxVolume);
            seekBar.setMin(0);
            seekBar.setProgress(volumeList.get(position));
            setImage(volumeList.get(position));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    volumeList.set(position, progress);
                    setImage(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekBar.setProgress(0);
                }
            });
        }

        private void setImage(int progress) {
            if (progress > 0) {
                imageView.setImageResource(imageFull);
            } else {
                imageView.setImageResource(imageEmpty);
            }
        }

        int getMax() {
            return audioManager.getStreamMaxVolume(streamMode);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        dialogView = layoutInflater.inflate(R.layout.volume_dialog, null);

        initializeDialogViews(builder, dialogView);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        okButton = view.findViewById(R.id.okButton);
        setDialogImage(imageView, R.drawable.volume_gif);
        buildDialog(builder, view, "Modify Volume", okButton);
    }


    protected void getUserInput() {
        ArrayList<String> results = new ArrayList<>();
        ArrayList<Long> percentages = new ArrayList<>();
        for (int i = 0; i < volumeList.size(); i++) {
            int vol = volumeList.get(i);
            results.add(String.valueOf(vol));
            percentages.add(Math.round((double) vol / seekBars.get(i).getMax() * 100));
        }
        String description = "Set Ring Volume to " + percentages.get(POSITION_RING) + "%\n" +
                "Set Media Volume to " + percentages.get(POSITION_MEDIA) + "%\n" +
                "Set Notifications Volume to " + percentages.get(POSITION_NOTIFICATIONS) + "%";
        listener.applyUserInfo(results, description);
    }
}
