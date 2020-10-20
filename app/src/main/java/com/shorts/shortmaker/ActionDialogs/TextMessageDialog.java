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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.shorts.shortmaker.Adapters.ContactsAdapter;
import com.shorts.shortmaker.DataClasses.Contact;
import com.shorts.shortmaker.R;
import com.shorts.shortmaker.SystemHandlers.ContactsHandler;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private List<Pair<String, String>> whoToSendList;

    private TextWatcher messageTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (whoToSendList.size() == 0) {
                whoToSendTo.setError("Choose a Contact");
            } else {
                okButton.setEnabled(!message.getText().toString().equals(""));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (message.getText().toString().equals("")) {
                message.setError("Message is Empty");
                okButton.setEnabled(false);
            } else {
                message.setError(null);
                okButton.setEnabled(true);
            }
        }
    };

    private ArrayList<String> data;
    private Map<String, String> reversedContacts;
    private Button cancelButton;

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

        data = getArguments().getStringArrayList("data");
        whoToSendList = new ArrayList<>();

        setDialogViews();

        buildDialog(builder, view, "Send Text Message", okButton);
        return builder.create();
    }


    protected void setDialogViews() {
        initializeDialogViews();
        setSearchContactBox();
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.text_message_gif);
    }

    private void initializeDialogViews() {
        whoToSendTo = view.findViewById(R.id.search_edit_text);
        message = view.findViewById(R.id.message);
        if (data != null && data.size() > 0) {
            message.setText(data.get(0));
        }
        message.addTextChangedListener(messageTextWatcher);
        cancelButton = view.findViewById(R.id.cancelButton);
        okButton = view.findViewById(R.id.okButton);
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
                if (adapter != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }
        });
    }

    private void createContactsList() {
        contactsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : ContactsHandler.getContacts().entrySet()) {
            contactsList.add(new Contact(entry.getKey(), entry.getValue()));
            fullContactsList.add(new Contact(entry.getKey(), entry.getValue()));
        }
        reversedContacts = ContactsHandler.getReverseContacts();

        if (data != null && data.size() > 0) {
            for (String number : data.subList(1, data.size())) {
                onContactChosenHandler(new Pair<>(reversedContacts.get(number), number));
            }
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
                fullContactsList.remove(position);
                adapter.notifyDataSetChanged();
                onContactChosenHandler(contact);

            }
        });
    }

    protected void onContactChosenHandler(Pair<String, String> contact) {
        whoToSendList.add(contact);
        final Chip chip = initializeChip(contact);
        final ChipGroup chipGroup = view.findViewById(R.id.chipGroup2);
        chipGroup.addView((View) chip);
        whoToSendTo.setText("");
        setChipOnClickListener(chip, chipGroup);
        if (message.getText().toString().equals("")) {
//                    message.requestFocus();
            message.setError("Message is Empty");
        } else {
            whoToSendTo.setError(null);
            okButton.setEnabled(true);
        }
    }

    @NotNull
    protected Chip initializeChip(Pair<String, String> contact) {
        final Chip chip = new Chip(getContext());
        chip.setText(contact.first);
        chip.setChipIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_textsms_24));
        // following lines are for the demo
        chip.setChipIconTintResource(R.color.gray);
        chip.setCloseIconVisible(true);
        chip.setClickable(true);
        chip.setCheckable(false);
        return chip;
    }

    protected void setChipOnClickListener(final Chip chip, final ChipGroup chipGroup) {
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView((View) chip);
                whoToSendTo.setError("Choose a Contact");
                okButton.setEnabled(false);
                for (Pair<String, String> contact : whoToSendList) {
                    if (contact.first.equals(chip.getText().toString())) {
                        whoToSendList.remove(contact);
                        fullContactsList.add(new Contact(contact.first, contact.second));
                        adapter.notifyItemInserted(fullContactsList.size());
                    }
                }
            }
        });
    }


    protected void getUserInput() {
        String theMessage = message.getText().toString();
        ArrayList<String> results = new ArrayList<>();
        results.add(theMessage);
        for (Pair<String, String> contact : whoToSendList) {
            results.add(contact.second);
        }
        String contact;
        if (whoToSendList.size() > 1) {
            contact = "Group";
        } else {
            contact = whoToSendList.get(0).first;
        }
        String description = "Send SMS to " + contact;
        listener.applyUserInfo(results, description);

    }


    private boolean showContacts(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || activity.checkSelfPermission(
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
            //After this point you wait for callback in onRequestPermissionsResult(int, String[],
            // int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            queryContacts();
            return true;
        }
    }

    private void queryContacts() {
        if (ContactsHandler.getContactNames(getContext())) {
            createContactsList();

        } else {
            //todo handel error
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
}
