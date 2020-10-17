package com.shorts.shortmaker.ActionDialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class ReadAutoDialog extends ActionDialog {

    private Button okButton;
    public static final int PERMISSIONS_REQUEST = 102;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            if (activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,
                                Manifest.permission.RECEIVE_SMS},
                        PERMISSIONS_REQUEST);
                okButton.setEnabled(false);
            } else {
                okButton.setEnabled(true);
            }
        } else {
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.read_auto_dialog, null);

        initializeDialogViews(view);
        buildDialog(builder, view, "Turn on read automatically", okButton);
        return builder.create();
    }

    protected void initializeDialogViews(View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.auto_reader_gif);
        okButton = view.findViewById(R.id.okButton);
    }

    protected void getUserInput() {
        ArrayList<String> results = new ArrayList<>();
        results.add("0");
        String description = "Read automatically On";
        listener.applyUserInfo(results, description);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                okButton.setEnabled(true);
            } else {
                Toast.makeText(getActivity(),
                        "Until you grant the permission, we cannot set this action",
                        Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }
}

