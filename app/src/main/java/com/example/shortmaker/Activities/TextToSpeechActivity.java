package com.example.shortmaker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.speech.tts.TextToSpeech;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shortmaker.MyReceiver;
import com.example.shortmaker.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TextToSpeechActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    TextView messageTextView, phoneNumTextView;
    MyReceiver receiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            messageTextView.setText(msg);
            phoneNumTextView.setText(phoneNumber);
        }
    };
    private Map<String, String> contacts;
    private TextToSpeech textToSpeech;
    private EditText editText;
    private SeekBar seekBarPitch;
    private SeekBar seekBarSpeed;
    private Button buttonSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speach);

        initializeUi();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }
        setTextToSpeech();
        showContacts();

    }




    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(SMS_RECEIVED));
    }

    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            contacts = getContactNames();
        }
    }


    private Map<String, String> getContactNames() {
        Map<String, String> contacts = new HashMap<>();
        ContentResolver cr = getContentResolver();
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


    private void setTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(TextToSpeechActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        //everything went well
                        buttonSpeak.setEnabled(true);
                    }
                } else {
                    Toast.makeText(TextToSpeechActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initializeUi() {
        buttonSpeak = findViewById(R.id.button_speak);

        editText = findViewById(R.id.edit_text);
        seekBarPitch = findViewById(R.id.seek_bar_pitch);
        seekBarSpeed = findViewById(R.id.seek_bar_speed);

        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

//        messageTextView = findViewById(R.id.message);
//        phoneNumTextView = findViewById(R.id.phoneNum);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thank you for permitting", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please permit", Toast.LENGTH_SHORT).show();
                }

            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted
                    showContacts();
                } else {
                    Toast.makeText(this, "Until you grant the permission, we cannot get the names", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void speak() {
//        String text = editText.getText().toString();
        float pitch = (float) seekBarPitch.getProgress() / 50;
        if (pitch < 0.1) {
            pitch = 0.1f;
        }
        float speed = (float) seekBarSpeed.getProgress() / 50;
        if (speed < 0.1) {
            speed = 0.1f;
        }

        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speed);
        if (!(phoneNumTextView.getText().equals("") || messageTextView.getText().equals(""))) {
            String a = getKey(phoneNumTextView.getText().toString());
            textToSpeech.speak("You got a message from: " + getKey(phoneNumTextView.getText().toString()) +
                            " and it says: " + messageTextView.getText(), TextToSpeech.QUEUE_FLUSH,
                    null);
        } else {
            textToSpeech.speak("You have no messages yet", TextToSpeech.QUEUE_FLUSH,
                    null);
        }
    }

    public String getKey(String value) {
        for (Map.Entry<String, String> entry : contacts.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();

        }
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}