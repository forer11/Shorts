package com.example.shortmaker.SystemHandlers;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.android.gms.dynamic.IFragmentWrapper;

public class BluetoothHandler {
    Context context;
    BluetoothAdapter bluetoothAdapter;

    public BluetoothHandler(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter BTintent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(broadcastReceiver, BTintent);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);

                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            Log.d("BT", "onReceive: STATE OFF");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.d("BT", "onReceive: STATE TURNING OFF");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Log.d("BT", "onReceive: STATE ON");
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d("BT", "onReceive: STATE TUNING ON");
                            break;
                    }
                }
            }
        }
    };

    public void enableBluetooth() {
        if (bluetoothAdapter == null) {
            Log.d("ERROR", "device doesn't have bluetooth capabilities");
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBluetoothIntent);
        }
    }

    public void disableBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    public void unregister() {
        context.unregisterReceiver(broadcastReceiver);
    }


}
