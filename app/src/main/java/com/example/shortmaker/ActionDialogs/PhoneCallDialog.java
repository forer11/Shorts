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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import static com.example.shortmaker.Actions.ActionPhoneCall.PERMISSIONS_REQUEST_READ_CONTACTS;

public class PhoneCallDialog extends ActionDialog {

    public static final int VALID_NUM_LENGTH = 7;
    public static final String DEFAULT_PREFIX = "Choose prefix";
    public static final String[] PREFIXES = new String[]{DEFAULT_PREFIX,"052", "054", "050"};

    private EditText phoneNum;
    private int prefixIdx;
    private Map<String, String> contacts;

    private ArrayList<Contact> contactsList;

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.phone_call_dialog, null);

        setPhonePrefixSpinner(view);
        phoneNum = view.findViewById(R.id.phoneNum);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.phone_call_gif).into(imageView);
        showContacts(getActivity());

        createContactsList();
        buildRecyclerView();
        EditText editText = view.findViewById(R.id.search_edit_text);
        editText.addTextChangedListener(new TextWatcher() {
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

        buildDialog(builder, view);
        return builder.create();
    }



    private void filter(String text) {
        ArrayList<Contact> filteredList = new ArrayList<>();
        for (Contact item : contactsList) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
    private void createContactsList() {
        contactsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : contacts.entrySet()) {
            contactsList.add(new Contact(entry.getKey(), entry.getValue()));

        }
    }
    private void buildRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ContactsAdapter(contactsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
        if(PREFIXES[prefixIdx].equals(DEFAULT_PREFIX) || numberToCall.length()!= VALID_NUM_LENGTH){
            Toast.makeText(getContext(), "Please ensert a valid number", Toast.LENGTH_SHORT).show();
        } else {
            results.add(PREFIXES[prefixIdx]);
            results.add(numberToCall);
            listener.applyUserInfo(results);
        }
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

    public void showContacts(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            contacts = getContactNames(activity);
        }
    }


    private Map<String, String> getContactNames(Activity activity) {
        Map<String, String> contacts = new HashMap<>();
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
        return contacts;
    }

}
