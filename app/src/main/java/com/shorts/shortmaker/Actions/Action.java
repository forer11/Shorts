package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.content.Context;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;

import java.util.List;

public interface Action {

    void activate(Context context, Context activity, boolean isNewTask);

    ActionDialog getDialog();

    void setData(List<String> data);

}
