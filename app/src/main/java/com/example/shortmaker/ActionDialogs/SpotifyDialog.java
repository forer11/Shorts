package com.example.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.shortmaker.R;

import java.util.ArrayList;


public class SpotifyDialog extends ActionDialog {

    private EditText albumToPlay;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.spotify_dialog,null);

        albumToPlay = view.findViewById(R.id.editText);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.spotify_gif).into(imageView);

        builder.setView(view)
                .setTitle("Which album to play on Spotify")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String album = albumToPlay.getText().toString();
                        ArrayList<String> results = new ArrayList<>();
                        results.add(album);
                        listener.applyUserInfo(results);
                    }
                });


        return builder.create();
    }
}
