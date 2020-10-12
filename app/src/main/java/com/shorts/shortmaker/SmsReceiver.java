package com.shorts.shortmaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    public String msg ="";
    public String phoneNumber="";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.requireNonNull(intent.getAction()).equals(SMS_RECEIVED)){
            Bundle  dataBundle = intent.getExtras();
            if(dataBundle!=null){
                Object[] mypdu = (Object[])dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[Objects.requireNonNull(mypdu).length];

                for (int i=0; i<mypdu.length; i++){
                    String format = dataBundle.getString("format");
                    message[i] = SmsMessage.createFromPdu((byte[])mypdu[i],format);
                    msg = message[i].getMessageBody();
                    phoneNumber = message[i].getOriginatingAddress();
                }
            }
        }
    }
}
