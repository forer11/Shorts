package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;

import java.util.List;

public interface Action {

    void activate(Application application, Context context);

    ActionDialog getDialog();

    void setData(List<String> data);

}
