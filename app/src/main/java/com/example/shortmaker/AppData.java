package com.example.shortmaker;

import android.app.Application;

import com.example.shortmaker.FireBaseHandlers.FireBaseAuthHandler;
import com.example.shortmaker.FireBaseHandlers.FireStoreHandler;

public class AppData extends Application{
    FireStoreHandler fireStoreHandler;
    FireBaseAuthHandler fireBaseAuthHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        fireBaseAuthHandler = new FireBaseAuthHandler(getApplicationContext());
        fireStoreHandler = new FireStoreHandler(getApplicationContext());
    }
}
