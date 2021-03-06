package com.shorts.shortmaker.ActionDialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;

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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.shorts.shortmaker.Adapters.ContactsAdapter;
import com.shorts.shortmaker.DataClasses.Contact;
import com.shorts.shortmaker.R;
import com.shorts.shortmaker.SystemHandlers.ContactsHandler;

import java.util.ArrayList;
import java.util.Map;


public class PhoneCallDialog extends ActionDialog implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    // Request code for READ_CONTACTS. It can be any number > 0.
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private EditText phoneNum;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    private ArrayList<Contact> fullContactsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private Pair<String, String> contact;
    private Button okButton;
    private EditText searchContactEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        view = layoutInflater.inflate(R.layout.phone_call_dialog, null);

        initializeDialogViews();
        buildDialog(builder, view, "Make a call", okButton);
        if (phoneNum.getText().toString().equals("")) {
            phoneNum.setError("Choose a Contact");
        }
        return builder.create();
    }

    protected void initializeDialogViews() {
        phoneNum = view.findViewById(R.id.phoneNum);
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.phone_call_gif);
        okButton = view.findViewById(R.id.okButton);
        setSearchContactBox();
    }

    protected void setSearchContactBox() {
        searchContactEditText = view.findViewById(R.id.search_edit_text);
        searchContactEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }
        });
    }

    public void createContactsList() {
        contactsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : ContactsHandler.getContacts().entrySet()) {
            contactsList.add(new Contact(entry.getKey(), entry.getValue()));
            fullContactsList.add(new Contact(entry.getKey(), entry.getValue()));
        }

    }

    public void buildRecyclerView() {
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
                phoneNum.setError(null);
                okButton.setEnabled(true);
                phoneNum.setText(contact.first);

            }
        });
    }


    protected void getUserInput() {
        String numberToCall = contact.second;
        ArrayList<String> results = new ArrayList<>();
        results.add(numberToCall);
        String description = "Call " + contact.first;
        listener.applyUserInfo(results, description);
    }


    private boolean showContacts(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED
                || activity.checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], i
            // nt[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            queryContacts();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                queryContacts();
                buildRecyclerView();
            } else {
                Toast.makeText(getActivity(),
                        "Until you grant the permission, we cannot set this action",
                        Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }

    private void queryContacts() {
        if (ContactsHandler.getContactNames(getContext())) {
            createContactsList();
        } else {
            //todo handel error
        }
    }
}
