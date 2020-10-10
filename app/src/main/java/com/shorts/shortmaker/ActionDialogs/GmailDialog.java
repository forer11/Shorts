package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.shorts.shortmaker.R;

import java.util.ArrayList;

public class GmailDialog extends ActionDialog {
    private EditText userEmail;
    private EditText userPassword;
    private EditText recipientEmail;
    private EditText messageSubject;
    private EditText messageBody;


    private Button okButton;
    private TextWatcher userInputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!(userEmail.equals("") || userPassword.equals("") || recipientEmail.equals("") ||
                    messageSubject.equals("") || messageBody.equals(""))) {
                okButton.setEnabled(true);
            }
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.gmail_dialog, null);

        initializeDialogViews(view);

        buildDialog(builder, view, "Send an email", okButton);
        return builder.create();
    }

    protected void initializeDialogViews(View view) {
        userEmail = view.findViewById(R.id.userEmail);
        userPassword = view.findViewById(R.id.userPassword);
        recipientEmail = view.findViewById(R.id.recipientEmail);
        messageSubject = view.findViewById(R.id.messageSubject);
        messageBody = view.findViewById(R.id.messageBody);

        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.gmail_gif);
        okButton = view.findViewById(R.id.okButton);
        userEmail.addTextChangedListener(userInputTextWatcher);
        userPassword.addTextChangedListener(userInputTextWatcher);
        recipientEmail.addTextChangedListener(userInputTextWatcher);
        messageSubject.addTextChangedListener(userInputTextWatcher);
        messageBody.addTextChangedListener(userInputTextWatcher);

    }


    protected void getUserInput() {
        String userEmailInput = userEmail.getText().toString();
        String userPasswordInput = userPassword.getText().toString();
        String recipientEmailInput = recipientEmail.getText().toString();
        String messageSubjectInput = messageSubject.getText().toString();
        String messageBodyInput = messageBody.getText().toString();
        ArrayList<String> results = new ArrayList<>();
        results.add(userEmailInput);
        results.add(userPasswordInput);
        results.add(recipientEmailInput);
        results.add(messageSubjectInput);
        results.add(messageBodyInput);

        String description = "Gmail"; //TODO decide
        listener.applyUserInfo(results, description);
    }
}
