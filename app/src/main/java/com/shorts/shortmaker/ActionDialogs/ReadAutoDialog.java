package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class ReadAutoDialog extends ActionDialog {

    private Button okButton;

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
}

