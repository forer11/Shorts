package com.shorts.shortmaker.ActionDialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shorts.shortmaker.Adapters.ContactsAdapter;
import com.shorts.shortmaker.DataClasses.Contact;
import com.shorts.shortmaker.R;
import com.shorts.shortmaker.SystemHandlers.ContactsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PhoneCallDialog extends ActionDialog implements ActivityCompat.OnRequestPermissionsResultCallback {

    // Request code for READ_CONTACTS. It can be any number > 0.
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private EditText phoneNum;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    private ArrayList<Contact> fullContactsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (showContacts(getActivity()))
        {
            buildRecyclerView();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.phone_call_dialog, null);

        phoneNum = view.findViewById(R.id.phoneNum);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.phone_call_gif).into(imageView);
        setSearchContactBox();

        buildDialog(builder, view);
        return builder.create();
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

    public void createContactsList() {
        contactsList = new ArrayList<>();
        for (Map.Entry<String, String> entry :  ContactsHandler.getContacts().entrySet()) {
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
        adapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                phoneNum.setText(contactsList.get(position).getContactNum());
            }
        });
    }

    protected void buildDialog(AlertDialog.Builder builder, View view) {
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
                        getUserInput();
                    }
                });
    }

    protected void getUserInput() {
        String numberToCall = phoneNum.getText().toString();
        ArrayList<String> results = new ArrayList<>();
        results.add(numberToCall);
        listener.applyUserInfo(results);
    }


    /**
     * //todo Carmel doc
     * @param activity the activity
     * @return true if permission is already granted, false otherwise
     */
    private boolean showContacts(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            queryContacts(activity);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                queryContacts(getActivity());
                buildRecyclerView();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we cannot get the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void queryContacts(Activity activity) {
        if (ContactsHandler.getContactNames(activity)) {
            createContactsList();
        } else {
            //todo handel error
        }
    }
}
