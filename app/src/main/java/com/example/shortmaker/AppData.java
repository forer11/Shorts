package com.example.shortmaker;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.example.shortmaker.DataClasses.Icon;
import com.example.shortmaker.DataClasses.Shortcut;
import com.example.shortmaker.FireBaseHandlers.FireBaseAuthHandler;
import com.example.shortmaker.FireBaseHandlers.FireStoreHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class AppData extends Application {
    public FireStoreHandler fireStoreHandler;
    public FireBaseAuthHandler fireBaseAuthHandler;
    public ArrayList<Icon> icons;

    @Override
    public void onCreate() {
        super.onCreate();
        fireBaseAuthHandler = new FireBaseAuthHandler(getApplicationContext());
        fireStoreHandler = new FireStoreHandler(getApplicationContext());
        printHashKey();
        icons = new ArrayList<>();
        loadIcons();
    }

    public void loadIcons() {
        fireStoreHandler.loadIcons(icons);
    }

    public ArrayList<Icon> getIcons(){
        return icons;
    }

    public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.shortmaker",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (
                PackageManager.NameNotFoundException e) {
            Log.d("ERROR", e.toString());

        } catch (
                NoSuchAlgorithmException e) {
            Log.d("ERROR", e.toString());
        }
    }
}
