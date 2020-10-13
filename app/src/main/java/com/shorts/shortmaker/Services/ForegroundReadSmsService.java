package com.shorts.shortmaker.Services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shorts.shortmaker.Activities.MainActivity;
import com.shorts.shortmaker.BroadcastReceivers.NotificationReceiver;
import com.shorts.shortmaker.SmsReceiver;
import com.shorts.shortmaker.R;
import com.shorts.shortmaker.SystemHandlers.ContactsHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.shorts.shortmaker.AppData.CHANNEL_ID;

public class ForegroundReadSmsService extends Service {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    private static final int NOTIF_ID = 1;
    public static final String MY_CLOSE_FOREGROUND = "my.close.foreground";

    private TextToSpeech textToSpeech;
    private String message = "";
    private String number = "";
    private static Map<String, String> reverseContacts = new HashMap<>();
    Context context;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver closeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), MY_CLOSE_FOREGROUND)) {
                stopService();
            }
        }
    };

    private void stopService() {
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        registerReceiver(receiver, new IntentFilter(SMS_RECEIVED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Dexter.withContext(this).withPermissions(Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void
                    onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (ContactsHandler.getContactNames()) {
                            reverseContacts.putAll(ContactsHandler.getReverseContacts());
                        }
                        setTextToSpeech();
                    }

                    @Override
                    public void
                    onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                       PermissionToken permissionToken) {

                    }
                }).check();


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
                .setSmallIcon(R.mipmap.pants)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.pants))
                .addAction(R.mipmap.teddy_bear, "Stop service", actionIntent)
                .build();

        startForeground(NOTIF_ID, notification);
    }

    private void setTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, "Language not supported",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //everything went well
                    }
                } else {
                    Toast.makeText(context, "Initialization failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    SmsReceiver receiver = new SmsReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            message = msg;
            number = phoneNumber;
            speak();
        }
    };

    private void speak() {
        String contact = number;
        if (reverseContacts.containsKey(number)) {
            contact = reverseContacts.get(number);
        }
        if (!(number.equals("") || message.equals(""))) {
            textToSpeech.speak("You got a message from: " + contact +
                            " and it says: " + message, TextToSpeech.QUEUE_FLUSH,
                    null, null);
        } else {
            textToSpeech.speak("You have no messages yet", TextToSpeech.QUEUE_FLUSH,
                    null, null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();

        }
        unregisterReceiver(receiver);
    }
}
