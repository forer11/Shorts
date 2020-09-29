package com.example.shortmaker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.Actions.ActionAlarmClock;
import com.example.shortmaker.Actions.ActionSendTextMessage;
import com.example.shortmaker.Actions.ActionSoundSettings;
import com.example.shortmaker.Actions.ActionSpotify;
import com.example.shortmaker.Actions.ActionWaze;
import com.example.shortmaker.Adapters.ActionAdapter;
import com.example.shortmaker.AppData;
import com.example.shortmaker.DataClasses.Shortcut;
import com.example.shortmaker.DialogFragments.ChooseActionDialog;
import com.example.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.example.shortmaker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;

import java.util.ArrayList;


public class SetActionsActivity extends AppCompatActivity
        implements ActionDialog.DialogListener,
        ChooseActionDialog.ChooseActionDialogListener {

    private static final String ICON_DIALOG_TAG = "icon-dialog";
    private Shortcut currentShortcut;
    private ImageView shortcutIcon;
    AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_actions);
        appData = (AppData) getApplicationContext();

        if (getIntent().getExtras() != null) {
            String id = getIntent().getStringExtra("shortcutId");
            appData.fireStoreHandler.getShortcut(id, new FireStoreHandler.SingleShortcutCallback() {
                @Override
                public void onAddedShortcut(String id, Shortcut shortcut, Boolean success) {
                    if (success) { //TODO check failure
                        currentShortcut = shortcut;
                        setRecyclerView();
                        TextView shortcutTitle = findViewById(R.id.shortcutTitle);
                        shortcutIcon = findViewById(R.id.shortcutIcon);
                        shortcutTitle.setText(shortcut.getTitle());
                        Glide.with(SetActionsActivity.this)
                                .load(shortcut.getImageUrl())
                                .into(shortcutIcon);
                    }
                }
            });
        }

        FloatingActionButton addActionButton = findViewById(R.id.addAction);
        showAddActionDialog(addActionButton);

    }


    private void setRecyclerView() {
        ArrayList<Action> exampleList = new ArrayList<>();
        exampleList.add(new ActionWaze(this));
        exampleList.add(new ActionSpotify(this));
        exampleList.add(new ActionSoundSettings(this));
        exampleList.add(new ActionAlarmClock(this));
        exampleList.add(new ActionSendTextMessage(this));
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new ActionAdapter(exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showAddActionDialog(FloatingActionButton addActionButton) {
        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoticeDialog();
            }
        });
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ChooseActionDialog();

        dialog.show(getSupportFragmentManager(), "choose action dialog");
    }

    private void showIconPickerDialog() {
        // If dialog is already added to fragment manager, get it. If not, create action new instance.
        IconDialog dialog = (IconDialog) getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
        IconDialog iconDialog = dialog != null ? dialog
                : IconDialog.newInstance(new IconDialogSettings.Builder().build());
        iconDialog.show(getSupportFragmentManager(), ICON_DIALOG_TAG);
    }

    @Override
    public void applyUserInfo(ArrayList<String> data) {
        Toast.makeText(this, data.get(0), Toast.LENGTH_SHORT).show(); //TODO - update in waze action the address
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}