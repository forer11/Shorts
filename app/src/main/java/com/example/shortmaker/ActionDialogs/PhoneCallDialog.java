package com.example.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.shortmaker.R;

import java.util.ArrayList;

public class PhoneCallDialog extends ActionDialog {

    public static final int VALID_NUM_LENGTH = 7;
    private EditText phoneNum;
    private int prefixIdx;
    public static final String DEFAULT_PREFIX = "Choose prefix";
    public static final String[] PREFIXES = new String[]{DEFAULT_PREFIX,"052", "054", "050"};

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.phone_call_dialog, null);

        setPhonePrefixSpinner(view);
        phoneNum = view.findViewById(R.id.phoneNum);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.phone_call_gif).into(imageView);

        builder.setView(view)
                .setTitle("Make a call")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String numberToCall = phoneNum.getText().toString();
                        ArrayList<String> results = new ArrayList<>();
                        if(PREFIXES[prefixIdx].equals(DEFAULT_PREFIX) || numberToCall.length()!= VALID_NUM_LENGTH){
                            Toast.makeText(getContext(), "Please ensert a valid number", Toast.LENGTH_SHORT).show();
                        } else {
                            results.add(PREFIXES[prefixIdx]);
                            results.add(numberToCall);
                            listener.applyUserInfo(results);
                        }
                    }
                });

        return builder.create();
    }

    private void setPhonePrefixSpinner(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.prefixSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, PREFIXES);
        // set Adapter
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Register a callback to be invoked when an item in this AdapterView has been selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                prefixIdx = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }
}
