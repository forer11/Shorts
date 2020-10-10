package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class SoundSettingsDialog extends ActionDialog {

    public static final String CHOOSE_MODE = "Choose Mode";
    private int mode;
    private Button okButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sound_settings_dialog, null);

        initializeDialogViews(builder, view);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        setModesSpinner(view);
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.rington_gif);
        okButton = view.findViewById(R.id.okButton);
        buildDialog(builder, view);
    }

    protected void buildDialog(AlertDialog.Builder builder, View view) {
        builder.setView(view)
                .setTitle("Which mode you would like");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInput();
                dismiss();
            }
        });
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    protected void getUserInput() {
        ArrayList<String> results = new ArrayList<>();
        results.add(String.valueOf(mode));
        listener.applyUserInfo(results);
    }

    protected void setModesSpinner(View view) {
        String[] modes = {CHOOSE_MODE,"Silent Mode", "Vibrate Mode", "Ring Mode"};
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, modes);
        // set Adapter
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinner(spinner);

    }

    private void setSpinner(final Spinner spinner) {
        //Register a callback to be invoked when an item in this AdapterView has been selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                setSpinnerSelectionListener(position, spinner);
                mode = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    protected void setSpinnerSelectionListener(int position, Spinner spinner) {
        String item = (String) spinner.getItemAtPosition(position);
        if (!item.equals(CHOOSE_MODE)) {
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(false);
        }
    }
}
