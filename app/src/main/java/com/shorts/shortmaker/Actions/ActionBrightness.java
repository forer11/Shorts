package com.shorts.shortmaker.Actions;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.BrightnessDialog;

import java.util.List;

public class ActionBrightness implements Action {


    private BrightnessDialog dialog;
    private int brightness;

    public ActionBrightness() {
        this.dialog = new BrightnessDialog();
    }


    @Override
    public void activate(Application application, Context context) {
        if (Settings.System.canWrite(context)) {
            ContentResolver cResolver = context.getApplicationContext().getContentResolver();
            Settings.System.putInt(cResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(cResolver,
                    Settings.System.SCREEN_BRIGHTNESS, brightness);
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,
                            "Enable Modify system settings and try again",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        brightness = Integer.parseInt(data.get(0));
    }
}
