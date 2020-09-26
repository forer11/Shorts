package com.example.shortmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.Actions.ActionAlarmClock;
import com.example.shortmaker.Actions.ActionPhoneCall;
import com.example.shortmaker.Actions.ActionSendTextMessage;
import com.example.shortmaker.Actions.ActionSoundSettings;
import com.example.shortmaker.Actions.ActionSpotify;
import com.example.shortmaker.Actions.ActionWaze;
import com.example.shortmaker.Views.MovableFloatingActionButton;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class SetActionsActivity extends AppCompatActivity implements IconDialog.Callback, ActionDialog.DialogListener {

    private static final String ICON_DIALOG_TAG = "icon-dialog";

    public static final int SILENT_MODE = 0;
    public static final int VIBRATE_MODE = 1;
    public static final int RING_MODE = 2;

    private ImageView shortcutIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_actions);
        if (getIntent().getExtras() != null) {
            Bitmap bitmap = (Bitmap) this.getIntent().getParcelableExtra("shortcutIcon");
            String title = getIntent().getStringExtra("shortcutName");
            TextView shortcutTitle = findViewById(R.id.shortcutTitle);
            shortcutIcon = findViewById(R.id.shortcutIcon);
            shortcutTitle.setText(title);
            shortcutIcon.setImageBitmap(bitmap);
        }

        MovableFloatingActionButton changeIconButton = findViewById(R.id.changeIcon);
        changeIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconPickerDialog();
            }
        });
        MovableFloatingActionButton addActionButton = findViewById(R.id.addAction);
        showAddActionDialog(addActionButton);
    }

    private void showAddActionDialog(MovableFloatingActionButton addActionButton) {
        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog dialogCompact = new SearchDialog(SetActionsActivity.this, "Search action",
                        "Search for an action", null, createSampleData(),
                        new SearchResultListener<Action>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   Action item, int position) {
                                switch (item.getTitle()){
                                    //TODO - add more actions here and update the action in the activity recycler view
                                    case "Waze action":
                                        wazeActionHandler();
                                        break;
                                    case "Spotify action":
                                        spotifyActionHandler();
                                        break;
                                    case "Set alarm clock action":
                                        alarmClockActionHandler();
                                        break;
                                    case "Send text message action":
                                        textMessageActionHandler();
                                        break;
                                    case "Sound settings action":
                                        soundSettingActionHandler();
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                dialogCompact.show();

            }
        });
    }

    private void soundSettingActionHandler() {
        ActionSoundSettings textMessage = new ActionSoundSettings(SetActionsActivity.this);
        ActionDialog dialogFragment = textMessage.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(getSupportFragmentManager(), "sound settings dialog");
        }
    }

    private void textMessageActionHandler() {
        ActionSendTextMessage textMessage = new ActionSendTextMessage(SetActionsActivity.this,false);
        ActionDialog dialogFragment = textMessage.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(getSupportFragmentManager(), "text message dialog");
        }
    }

    private void alarmClockActionHandler() {
        ActionAlarmClock alarmClock = new ActionAlarmClock(SetActionsActivity.this);
        ActionDialog dialogFragment = alarmClock.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(getSupportFragmentManager(), "alarm clock dialog");
        }
    }

    private void spotifyActionHandler() {
        ActionSpotify spotify = new ActionSpotify(SetActionsActivity.this);
        ActionDialog dialogFragment = spotify.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(getSupportFragmentManager(), "spotify dialog");
        }
    }

    private void wazeActionHandler() {
        ActionWaze waze = new ActionWaze(SetActionsActivity.this);
        ActionDialog dialogFragment = waze.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(getSupportFragmentManager(), "waze dialog");
        }
    }


    private ArrayList<Action> createSampleData() {
        ArrayList<Action> items = new ArrayList<>();
        items.add(new ActionWaze(SetActionsActivity.this));
        items.add(new ActionSpotify(SetActionsActivity.this));
        items.add(new ActionAlarmClock(SetActionsActivity.this));
        items.add(new ActionPhoneCall(SetActionsActivity.this));
        items.add(new ActionSendTextMessage(SetActionsActivity.this,false));
        items.add(new ActionSoundSettings(SetActionsActivity.this));
        return items;
    }

    private void showIconPickerDialog() {
        // If dialog is already added to fragment manager, get it. If not, create action new instance.
        IconDialog dialog = (IconDialog) getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
        IconDialog iconDialog = dialog != null ? dialog
                : IconDialog.newInstance(new IconDialogSettings.Builder().build());
        iconDialog.show(getSupportFragmentManager(), ICON_DIALOG_TAG);
    }


    @Nullable
    @Override
    public IconPack getIconDialogIconPack() {
        return ((AppData) getApplication()).getIconPack();
    }

    @Override
    public void onIconDialogIconsSelected(@NonNull IconDialog dialog, @NonNull List<Icon> icons) {
        shortcutIcon.setImageDrawable(icons.get(0).getDrawable());
    }


    @Override
    public void onIconDialogCancelled() {

    }

    @Override
    public void applyUserInfo(ArrayList<String> data) {
        Toast.makeText(this, data.get(0), Toast.LENGTH_SHORT).show(); //TODO - update in waze action the address
    }
}