package com.shorts.shortmaker.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationLocationReceiver extends BroadcastReceiver {
    static public String EXTRA = "extra";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            String action = intent.getExtras().getString(EXTRA);
            if (action != null) {
                context.sendBroadcast(new Intent(action));
            }
        }
    }
}
