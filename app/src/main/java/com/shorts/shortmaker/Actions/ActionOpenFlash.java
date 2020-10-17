package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.FlashDialog;

import java.util.List;


public class ActionOpenFlash implements Action {
    private FlashDialog dialog;
    private int turnOn;


    public ActionOpenFlash() {
        this.dialog = new FlashDialog();
    }

    @Override
    public void activate(Application application, Context context , boolean isNewTask) {
        if(turnOn==0){
            String mCameraId="";
            CameraManager mCameraManager= (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraId = mCameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            try {
                mCameraManager.setTorchMode(mCameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        turnOn = Integer.parseInt(data.get(0));
    }
}
