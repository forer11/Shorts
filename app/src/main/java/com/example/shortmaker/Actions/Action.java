package com.example.shortmaker.Actions;

import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.ActionDialog;

import java.util.ArrayList;
import java.util.List;

public interface Action {

    void activate();

    ActionDialog getDialog();

    void setData(List<String> data);
}
