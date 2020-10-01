package com.example.shortmaker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.Adapters.ActionAdapter;
import com.example.shortmaker.AppData;
import com.example.shortmaker.DataClasses.ActionData;
import com.example.shortmaker.DataClasses.Shortcut;
import com.example.shortmaker.DialogFragments.ChooseActionDialog;
import com.example.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.example.shortmaker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;

import java.util.ArrayList;
import java.util.Objects;


public class SetActionsActivity extends AppCompatActivity
        implements ActionDialog.DialogListener,
        ChooseActionDialog.ChooseActionDialogListener {

    private static final String ICON_DIALOG_TAG = "icon-dialog";
    private Shortcut currentShortcut;
    private ImageView shortcutIcon;
    AppData appData;
    private ArrayList<ActionData> actions;
    private EditText shortcutTitle;
    private RecyclerView.Adapter adapter;

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
                        addedShortcutHandler(shortcut);
                    }
                }
            });
        }
        FloatingActionButton addActionButton = findViewById(R.id.addAction);
        showAddActionDialog(addActionButton);

    }

    private void addedShortcutHandler(Shortcut shortcut) {
        currentShortcut = shortcut;

        setRecyclerView();
        ShortcutTitleHandler(shortcut);
        shortcutIcon = findViewById(R.id.shortcutIcon);
        Glide.with(SetActionsActivity.this)
                .load(shortcut.getImageUrl())
                .into(shortcutIcon);
        shortcutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO - Lior - change icon
            }
        });
    }


    private void ShortcutTitleHandler(Shortcut shortcut) {
        shortcutTitle = findViewById(R.id.shortcutTitle);
        shortcutTitle.setText(shortcut.getTitle());
        final ImageButton updateTitleButton = findViewById(R.id.updateButton);
        final ImageButton clearTitleButton = findViewById(R.id.cancelButton);
        titleOnFocusChange(updateTitleButton, clearTitleButton);
        updateTitle(updateTitleButton, clearTitleButton);
        clearTitleUpdate(updateTitleButton, clearTitleButton);
    }

    private void clearTitleUpdate(final ImageButton updateTitleButton, final ImageButton clearTitleButton) {
        clearTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTitleView(v, updateTitleButton, clearTitleButton);
                Toast.makeText(SetActionsActivity.this, "Title update cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTitle(final ImageButton updateTitleButton, final ImageButton clearTitleButton) {
        updateTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentShortcut.setTitle(shortcutTitle.getText().toString());
                appData.fireStoreHandler.updateShortcut(currentShortcut);
                updateTitleView(v, updateTitleButton, clearTitleButton);
                Toast.makeText(SetActionsActivity.this, "Title changed successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTitleView(View v, ImageButton updateTitleButton, ImageButton clearTitleButton) {
        shortcutTitle.clearFocus();
        shortcutTitle.setText(currentShortcut.getTitle());
        updateTitleButton.setVisibility(View.INVISIBLE);
        clearTitleButton.setVisibility(View.INVISIBLE);
        hideKeyboard(v);
    }

    private void titleOnFocusChange(final ImageButton updateTitleButton, final ImageButton clearTitleButton) {
        shortcutTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
                if (hasFocus) {
                    shortcutTitle.setText("");
                    updateTitleButton.setVisibility(View.VISIBLE);
                    clearTitleButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void setRecyclerView() {
        actions = currentShortcut.getActionDataList();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        adapter = new ActionAdapter(actions);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void showAddActionDialog(FloatingActionButton addActionButton) {
        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new ChooseActionDialog();
                dialog.show(getSupportFragmentManager(), "choose action dialog");
            }
        });
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
    public void onChoseAction(final ActionData action, int position) {
        ActionDialog actionDialog = Objects
                .requireNonNull(ActionFactory.getAction(action.getTitle())).getDialog();
        if (actionDialog != null) {
            actionDialog.show(this.getSupportFragmentManager(), action.getTitle() + " dialog");
            actionDialog.setNewDialogListener(new ActionDialog.DialogListener() {
                @Override
                public void applyUserInfo(ArrayList<String> data) {
                    action.setData(data);
                    addAction(action);
                }
            });
        }
        else {
            addAction(action);
        }
    }

    private void addAction(ActionData action) {
        currentShortcut.getActionDataList().add(action);
        adapter.notifyItemInserted(currentShortcut.getActionDataList().size() - 1);
        appData.fireStoreHandler.updateShortcut(currentShortcut);
    }
}