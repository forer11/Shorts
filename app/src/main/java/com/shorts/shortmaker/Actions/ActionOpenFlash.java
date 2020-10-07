package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.ChangeOrientationDialog;
import com.shorts.shortmaker.ActionDialogs.FlashActionDialog;

import java.util.List;


public class ActionOpenFlash implements Action {
    private FlashActionDialog dialog;
    private int turnOn;


    public ActionOpenFlash() {
        this.dialog = new FlashActionDialog();
    }

    @Override
    public void activate(Context context, Activity activity) {
        if(turnOn==0){
            String mCameraId="";
            CameraManager mCameraManager= (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
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
