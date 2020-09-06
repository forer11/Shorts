package com.example.shortmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OpeningScreenActivity extends AppCompatActivity {
    AppData appData;
    FirebaseAuth firebaseAuth;
    private BroadcastReceiver br;
    private boolean registered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppData();
        openNextActivity();
    }

    private void checkLoginStatus(FirebaseUser currentUser) {
        Intent intent;
        if (currentUser != null) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void getAppData() {
        appData = (AppData) getApplicationContext();
        firebaseAuth = appData.fireBaseAuthHandler.firebaseAuth;
    }


    private void openNextActivity() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        checkLoginStatus(currentUser);
        finish();
    }


    @Override
    public void onBackPressed() {
        // we do not allow back press here
    }
}