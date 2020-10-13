package com.shorts.shortmaker.ActionDialogs;

import android.app.Activity;
import android.app.Dialog;
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

import com.shorts.shortmaker.R;

import java.util.ArrayList;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static com.shorts.shortmaker.Actions.ActionChangeOrientation.PORTRAIT_ORIENTATION;


public class ChangeOrientationDialog extends ActionDialog {

    public static final String CHANGE_ORIENTATION = "Change orientation";
    private int mode;
    private Button okButton;
    private String[] modes;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.orientation_dialog, null);

        initializeDialogViews(builder, view);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        setModesSpinner(view);
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.orientation_gif);
        okButton = view.findViewById(R.id.okButton);
        buildDialog(builder, view, CHANGE_ORIENTATION, okButton);
    }


    protected void getUserInput() {
        ArrayList<String> results = new ArrayList<>();
        results.add(String.valueOf(mode - 1));
        String orientation = modes[mode];
        String description = "set for " + orientation;
        listener.applyUserInfo(results, description);
    }

    protected void setModesSpinner(View view) {
        modes = new String[]{CHANGE_ORIENTATION, "Portrait", "Landscape"};
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, modes);
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
        if (!item.equals(CHANGE_ORIENTATION)) {
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(false);
        }
    }
}
