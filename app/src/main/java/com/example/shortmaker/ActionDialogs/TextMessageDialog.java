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

public class TextMessageDialog extends ActionDialog {


    private EditText whoToSendTo;
    private EditText message;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.text_message_dialog,null);

        whoToSendTo = view.findViewById(R.id.whoToSendTo);
        message = view.findViewById(R.id.message);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.text_message_gif).into(imageView);

        builder.setView(view)
                .setTitle("Send Text Message")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String whoToSend = whoToSendTo.getText().toString();
                        String theMessage = message.getText().toString();
                        ArrayList<String> results = new ArrayList<>();
                        results.add(whoToSend);
                        results.add(theMessage);
                        listener.applyUserInfo(results);
                    }
                });


        return builder.create();
    }



    public interface TextMessageDialogListener {
        void applyText(String address);
    }
}
