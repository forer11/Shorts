package com.example;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.shortmaker.R;

import java.util.ArrayList;


public class WazeDialog extends AppCompatDialogFragment {

    private WazeDialogListener listener;
    private EditText editTextAddress;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.waze_dialog,null);

        editTextAddress = view.findViewById(R.id.editText);

        builder.setView(view)
                .setTitle("Open Waze")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String address = editTextAddress.getText().toString();
                        ArrayList<String> results = new ArrayList<>();
                        results.add(address);

                    }
                });


        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (WazeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement WazeDialogListener");
        }
    }

    public interface WazeDialogListener {
        void applyText(String address);
    }
}
