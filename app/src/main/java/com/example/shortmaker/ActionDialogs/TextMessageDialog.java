package com.example.shortmaker.ActionDialogs;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shortmaker.Adapters.ContactsAdapter;
import com.example.shortmaker.DataClasses.Contact;
import com.example.shortmaker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TextMessageDialog extends ActionDialog {

    // Request code for READ_CONTACTS. It can be any number > 0.
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Map<String, String> contacts;

    private ArrayList<Contact> contactsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    private EditText whoToSendTo;
    private EditText message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showContacts(getActivity());

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.text_message_dialog, null);

        whoToSendTo = view.findViewById(R.id.search_edit_text);
        message = view.findViewById(R.id.message);

        buildRecyclerView();
        setSearchContactBox();

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
                        getUserInput();
                    }
                });


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
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<Contact> filteredList = new ArrayList<>();
        for (Contact item : contactsList) {
            if (item.getContactName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    public void createContactsList() {
        contactsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : contacts.entrySet()) {
            contactsList.add(new Contact(entry.getKey(), entry.getValue()));
        }

    }

    public void buildRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ContactsAdapter(contactsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                whoToSendTo.setText(contactsList.get(position).getContactNum());
            }
        });
    }


    protected void getUserInput() {
        String whoToSend = whoToSendTo.getText().toString();
        String theMessage = message.getText().toString();
        ArrayList<String> results = new ArrayList<>();
        results.add(whoToSend);
        results.add(theMessage);
        listener.applyUserInfo(results);
    }


    public void showContacts(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[],
            // int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            getContactNames(activity);
            createContactsList();
        }
    }


    public void getContactNames(Activity activity) {
        contacts = new HashMap<>();
        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts.put(name, phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getContactNames(getActivity());
                createContactsList();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(),
                        "Until you grant the permission, we cannot get the names",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
