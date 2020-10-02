package com.example.shortmaker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.example.shortmaker.Adapters.ActionAdapter;
import com.example.shortmaker.AppData;
import com.example.shortmaker.DataClasses.ActionData;
import com.example.shortmaker.DataClasses.Shortcut;
import com.example.shortmaker.DialogFragments.ChooseActionDialog;
import com.example.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.example.shortmaker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;


public class SetActionsActivity extends AppCompatActivity {

    private static final String ICON_DIALOG_TAG = "icon-dialog";
    private Shortcut currentShortcut;
    AppData appData;
    private EditText shortcutTitle;
    private RecyclerView.Adapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_set_actions);
        appData = (AppData) getApplicationContext();
        getShortcutObject();
        FloatingActionButton addActionButton = findViewById(R.id.addAction);
        showAddActionDialog(addActionButton);

    }

    private void getShortcutObject() {
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
    }

    private void addedShortcutHandler(Shortcut shortcut) {
        currentShortcut = shortcut;

        setRecyclerView();
        ShortcutTitleHandler(shortcut);
        shortcutImageHandler(shortcut);
    }

    private void shortcutImageHandler(Shortcut shortcut) {
        ImageView shortcutIcon = findViewById(R.id.shortcutIcon);
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
        Objects.requireNonNull(inputMethodManager)
                .hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        adapter = new ActionAdapter(currentShortcut.getActionDataList());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void showAddActionDialog(FloatingActionButton addActionButton) {
        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseActionDialog dialog = new ChooseActionDialog();
                dialog.show(getSupportFragmentManager(), "choose action dialog");
                setActionDialogListener(dialog);
            }

            private void setActionDialogListener(ChooseActionDialog dialog) {
                dialog.setNewChooseActionDialogListener(new ChooseActionDialog
                        .ChooseActionDialogListener() {
                    @Override
                    public void onChoseAction(final ActionData action, int position) {
                        ActionDialog actionDialog = Objects
                                .requireNonNull(ActionFactory
                                        .getAction(action.getTitle())).getDialog();
                        if (actionDialog != null) {
                            actionDialog.show(getSupportFragmentManager(),
                                    action.getTitle() + " dialog");
                            actionDialog.setNewDialogListener(new ActionDialog.DialogListener() {
                                @Override
                                public void applyUserInfo(ArrayList<String> data) {
                                    action.setData(data);
                                    addAction(action);
                                }
                            });
                        } else {
                            addAction(action);
                        }
                    }
                });
            }
        });
    }

    private void addAction(ActionData action) {
        currentShortcut.getActionDataList().add(action);
        adapter.notifyItemInserted(currentShortcut.getActionDataList().size() - 1);
        appData.fireStoreHandler.updateShortcut(currentShortcut);
    }
}