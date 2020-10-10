package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class FlashActionDialog extends ActionDialog {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.flash_dialog, null);

        initializeDialogViews(view);
        buildDialog(builder, view);
        return builder.create();
    }

    protected void initializeDialogViews(View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.flash_gif);
    }

    protected void buildDialog(AlertDialog.Builder builder, View view) {
        builder.setView(view)
                .setTitle("Turn on flash")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> results = new ArrayList<>();
                        results.add("0");
                        listener.applyUserInfo(results);
                    }
                });
    }
}

