package com.shorts.shortmaker.SystemHandlers;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ContactsHandler {
    private static Map<String, String> contacts = new HashMap<>();
    private static boolean loadedContacts = false;
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.DISPLAY_NAME,
    };

    /**
     * Fills the contacts list
     *
     * @param activity the activity
     * @return false if an error accord true otherwise.
     */
    static public boolean getContactNames(Activity activity) {
        if (!loadedContacts) {
            ContentResolver cr = activity.getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PROJECTION,
                    null,
                    null,
                    null);
            if (cursor != null) {
                try {
                    final int contactNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    String number;
                    String displayName;
                    while (cursor.moveToNext()) {
                        number = cursor.getString(contactNumber);
                        displayName = cursor.getString(displayNameIndex);
                        System.out.println("FALOL");
                        contacts.put(displayName, number);
                    }
                    loadedContacts = true;
                    cursor.close();
                    return true;

                } catch (Exception e) {
                    Log.v("Contacts Error", e.toString());
                    cursor.close();
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    public static Map<String, String> getContacts() {
        return contacts;
    }

    public static boolean areContactsLoaded() {
        return loadedContacts;
    }
}
