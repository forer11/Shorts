package com.shorts.shortmaker.Actions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.GpsDialog;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ActionGps implements Action {

    private GpsDialog dialog;
    private int turnOn;
    private boolean isGPS;
    private GoogleApiClient googleApiClient;


    public ActionGps() {
        this.dialog = new GpsDialog();
    }

    @Override
    public void activate(Context context, final Activity activity) {
       
    }

        @Override
        public ActionDialog getDialog () {
            return dialog;
        }

        @Override
        public void setData (List < String > data) {
            turnOn = Integer.parseInt(data.get(0));
        }
    }