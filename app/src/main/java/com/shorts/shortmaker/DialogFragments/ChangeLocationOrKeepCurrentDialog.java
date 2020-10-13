package com.shorts.shortmaker.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.Activities.SetActionsActivity;
import com.shorts.shortmaker.AppData;
import com.shorts.shortmaker.DataClasses.Shortcut;
import com.shorts.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.shorts.shortmaker.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChangeLocationOrKeepCurrentDialog extends AppCompatDialogFragment {

    public static final String USER_MSG = "We allow only one location Event per Shortcut," +
            " What would you like to do?";
    private Context context;
    private DialogListener listener;
    private Button okButton;

    public interface DialogListener {
        void getResponse(Boolean change);
    }

    public void setNewDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.change_or_keep_dialog, null);
        TextView textView = view.findViewById(R.id.shortcutTitle);
        textView.setText(USER_MSG);
        final AppData appData = (AppData) getApplicationContext();
        setDialogButtons(view, appData);
        builder.setView(view)
                .setTitle("Choose the following");
        return builder.create();
    }

    protected void setDialogButtons(View view, final AppData appData) {
        okButton = view.findViewById(R.id.okButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getResponse(false);
                dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getResponse(true);
                dismiss();
            }
        });
    }


}
