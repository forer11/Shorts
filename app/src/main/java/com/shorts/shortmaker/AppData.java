package com.shorts.shortmaker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.shorts.shortmaker.DataClasses.Icon;
import com.shorts.shortmaker.FireBaseHandlers.FireBaseAuthHandler;
import com.shorts.shortmaker.FireBaseHandlers.FireStoreHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

public class AppData extends Application {
    public FireStoreHandler fireStoreHandler;
    public FireBaseAuthHandler fireBaseAuthHandler;
    public ArrayList<Icon> icons;
    public static String CHANNEL_ID = "default_id";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        fireBaseAuthHandler = new FireBaseAuthHandler(getApplicationContext());
        fireStoreHandler = new FireStoreHandler(getApplicationContext());
        printHashKey();
        icons = new ArrayList<>();
        loadIcons();
        createNotificationChannel();
    }

    public void loadIcons() {
        fireStoreHandler.loadIcons(icons);
    }

    public ArrayList<Icon> getIcons() {
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
                PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.d("ERROR", e.toString());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "service channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(manager).createNotificationChannel(serviceChannel);
        }
    }

    public static Context getContext()
    {
        return context;
    }
}
