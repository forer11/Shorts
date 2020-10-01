package com.example.shortmaker.Actions;

import android.content.Context;

import androidx.fragment.app.DialogFragment;

import com.example.shortmaker.ActionDialogs.ActionDialog;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public interface Action {

    void activate(Context context);

    ActionDialog getDialog();

    void setData(List<String> data);

    String getTitle();

}
