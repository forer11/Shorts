package com.shorts.shortmaker.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shorts.shortmaker.BroadcastReceivers.NotificationReceiver;
import com.shorts.shortmaker.R;

import java.security.Provider;
import java.util.List;
import java.util.Objects;


public class ForegroundLocationListenerService extends Service {
    static final int LOCATION_SERVICE_ID = 175;
    public static final String START_LOCATION_SERVICE = "startLocationService";
    public static final String STOP_LOCATION_SERVICE = "stopLocationService";
    static final String CHANNEL_ID = "shorts.locationChannel";
    public static final String CLOSE_LOCATION_FOREGROUND = "CLOSE_LOCATION_FOREGROUND";
    private Context context;


    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longtitude = locationResult.getLastLocation().getLongitude();
                Log.v("location service", latitude + ", " + longtitude);
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startLocationService() {
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra(NotificationReceiver.EXTRA, CLOSE_LOCATION_FOREGROUND);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = CHANNEL_ID;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, channelId);
        builder.setSmallIcon(R.mipmap.teddy_bear);
        builder.setContentTitle("Listening to your Location...");
        builder.addAction(R.mipmap.delete_icon, "Stop service", actionIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");

        if (Build.VERSION.SDK_INT > -Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")// checked with dexter
                    @Override
                    public void
                    onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        LocationServices.getFusedLocationProviderClient(context)
                                .requestLocationUpdates(locationRequest,
                                        locationCallback,
                                        Looper.getMainLooper());
                        startForeground(LOCATION_SERVICE_ID, builder.build());
                    }

                    @Override
                    public void
                    onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                       PermissionToken permissionToken) {

                    }
                }).check();
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopSelf();
    }

    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), CLOSE_LOCATION_FOREGROUND)) {
                stopLocationService();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                startLocationService();
            }
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(CLOSE_LOCATION_FOREGROUND);
        this.registerReceiver(closeReceiver, filter);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(closeReceiver);
    }
}
