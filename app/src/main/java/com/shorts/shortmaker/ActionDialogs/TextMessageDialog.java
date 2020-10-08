package com.shorts.shortmaker.ActionDialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shorts.shortmaker.Adapters.ContactsAdapter;
import com.shorts.shortmaker.DataClasses.Contact;
import com.shorts.shortmaker.R;
import com.shorts.shortmaker.SystemHandlers.ContactsHandler;

import java.util.ArrayList;
import java.util.Map;

import javax.xml.validation.Validator;

public class TextMessageDialog extends ActionDialog {

    // Request code for READ_CONTACTS. It can be any number > 0.
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    private ArrayList<Contact> fullContactsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ContactsAdapter adapter = null;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    private EditText whoToSendTo;
    private EditText message;
    private Pair<String, String> contact;
    private Button okButton;

    private  TextWatcher messageTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(whoToSendTo.getText().toString().equals("")) {
                whoToSendTo.setError("Choose a Contact");
            } else {
                okButton.setEnabled(!message.getText().toString().equals(""));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            message.setError(null);

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (showContacts(getActivity())) {
            buildRecyclerView();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.text_message_dialog, null);

        whoToSendTo = view.findViewById(R.id.search_edit_text);
        message = view.findViewById(R.id.message);
        message.addTextChangedListener(messageTextWatcher);

        okButton = view.findViewById(R.id.okButton);
        setOkButton();
        Button cancelButton = view.findViewById(R.id.cancelButton);
        setCancelButton(cancelButton);
        setOkButton();
        setSearchContactBox();
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.text_message_gif).into(imageView);

        builder.setView(view)
                .setTitle("Send Text Message");

        return builder.create();
    }

    protected void setCancelButton(Button cancelButton) {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    protected void setOkButton() {
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInput();
                dismiss();
            }
        });
    }

    protected void setSearchContactBox() {
        EditText searchContactEditText = view.findViewById(R.id.search_edit_text);
        searchContactEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
    }

    private void createContactsList() {
        contactsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : ContactsHandler.getContacts().entrySet()) {
            contactsList.add(new Contact(entry.getKey(), entry.getValue()));
            fullContactsList.add(new Contact(entry.getKey(), entry.getValue()));
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    private void buildRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ContactsAdapter(contactsList, fullContactsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                contact = new Pair<>(contactsList.get(position).getContactName(),
                        contactsList.get(position).getContactNum());
                whoToSendTo.setText(contact.first);
                if(message.getText().toString().equals("")){
                    message.requestFocus();
                    message.setError("Message is Empty");
                } else {
                    whoToSendTo.setError(null);
                    okButton.setEnabled(true);
                }
            }
        });
    }


    protected void getUserInput() {
        String theMessage = contact.first;
        String whoToSend = contact.second;
        ArrayList<String> results = new ArrayList<>();
        results.add(whoToSend);
        results.add(theMessage);
        listener.applyUserInfo(results);

    }


    /**
     * //todo Carmel doc
     *
     * @param activity the activity
     * @return true if permission is already granted, false otherwise
     */
    private boolean showContacts(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
            //After this point you wait for callback in onRequestPermissionsResult(int, String[],
            // int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            queryContacts(activity);
            return true;
        }
    }

    private void queryContacts(Activity activity) {
        if (ContactsHandler.getContactNames(activity)) {
            createContactsList();
        } else {
            //todo handel error
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                queryContacts(getActivity());
                buildRecyclerView();
            } else {
                Toast.makeText(getActivity(),
                        "Until you grant the permission, we cannot get the names",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
