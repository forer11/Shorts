package com.shorts.shortmaker.Actions;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.ChangeOrientationDialog;

import java.util.List;

public class ActionChangeOrientation implements Action {

    public static final int AUTO_ROTATION_OFF = 0;
    private ChangeOrientationDialog dialog;
    private boolean desiredOrientation;


    public ActionChangeOrientation() {
        this.dialog = new ChangeOrientationDialog();
    }

    @Override
    public void activate(Application application, Context context) {
        if (Settings.System.canWrite(context)) {
            setAutoOrientationEnabled(context, desiredOrientation);
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
        desiredOrientation = !(Integer.parseInt(data.get(0)) == AUTO_ROTATION_OFF);
    }

    public static void setAutoOrientationEnabled(Context context, boolean enabled) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }
}
