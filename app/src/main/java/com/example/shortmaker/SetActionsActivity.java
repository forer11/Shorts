package com.example.shortmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.AlarmClockDialog;
import com.example.shortmaker.ActionDialogs.WazeDialog;
import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.Actions.ActionAlarmClock;
import com.example.shortmaker.Actions.ActionPhoneCall;
import com.example.shortmaker.Actions.ActionSendTextMessage;
import com.example.shortmaker.Actions.ActionSoundSettings;
import com.example.shortmaker.Actions.ActionSpotify;
import com.example.shortmaker.Actions.ActionWaze;
import com.example.shortmaker.Adapters.ActionAdapter;
import com.example.shortmaker.Views.MovableFloatingActionButton;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;



public class SetActionsActivity extends AppCompatActivity
        implements IconDialog.Callback, ActionDialog.DialogListener,
        ChooseActionDialog.ChooseActionDialogListener {

    private static final String ICON_DIALOG_TAG = "icon-dialog";
    private ImageView shortcutIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_actions);
        if (getIntent().getExtras() != null) {
            getShortcutData();
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

        setRecyclerView();
    }

    private void getShortcutData() {
        Bitmap bitmap = (Bitmap) this.getIntent().getParcelableExtra("shortcutIcon");
        String title = getIntent().getStringExtra("shortcutName");
        TextView shortcutTitle = findViewById(R.id.shortcutTitle);
        shortcutIcon = findViewById(R.id.shortcutIcon);
        shortcutTitle.setText(title);
        shortcutIcon.setImageBitmap(bitmap);
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

    private void showAddActionDialog(MovableFloatingActionButton addActionButton) {
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}