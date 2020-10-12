package com.shorts.shortmaker.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.shorts.shortmaker.Activities.MainActivity;
import com.shorts.shortmaker.BroadcastReceivers.NotificationReceiver;
import com.shorts.shortmaker.R;

import java.util.Objects;

import static com.shorts.shortmaker.AppData.CHANNEL_ID;

public class ForegroundService extends Service {

    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    public static final String MY_CLOSE_FOREGROUND = "my.close.foreground";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), MY_CLOSE_FOREGROUND)) {
                stopForeground(true);
                stopSelf();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // do your jobs here
        IntentFilter filter = new IntentFilter();
        filter.addAction(MY_CLOSE_FOREGROUND);
        this.registerReceiver(closeReceiver, filter);
        startForeground();

        return START_NOT_STICKY;
    }

    private void startForeground() {
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("command", MY_CLOSE_FOREGROUND);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("listening")
                .setContentText("yay")
                .setSmallIcon(R.drawable.bluetooth_icon)
                .addAction(R.mipmap.teddy_bear, "Stop service", actionIntent)
                .build();

        startForeground(NOTIF_ID, notification);
    }


}
