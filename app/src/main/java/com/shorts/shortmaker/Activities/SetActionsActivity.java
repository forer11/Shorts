package com.shorts.shortmaker.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionFactory;
import com.shorts.shortmaker.Actions.Action;
import com.shorts.shortmaker.Adapters.ActionAdapter;
import com.shorts.shortmaker.AppData;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.DataClasses.Shortcut;
import com.shorts.shortmaker.DialogFragments.ChooseActionDialog;
import com.shorts.shortmaker.DialogFragments.ChooseIconDialog;
import com.shorts.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.shorts.shortmaker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
//

public class SetActionsActivity extends AppCompatActivity implements ChooseIconDialog.OnIconPick,
        PopupMenu.OnMenuItemClickListener {

    private static final String ICON_DIALOG_TAG = "icon-dialog";
    public static final int NO_POSITION = -1;

    int lastPosition = NO_POSITION;

    private Shortcut currentShortcut;
    AppData appData;
    private EditText shortcutTitle;
    private ActionAdapter adapter;
    private ImageView shortcutIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_actions);
        setBackButton();
        appData = (AppData) getApplicationContext();
        getShortcutObject();
        showAddActionDialog();
    }

    private void setBackButton() {
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        shortcutIcon = findViewById(R.id.shortcutIcon);
        setShortcutImage(shortcut);
        shortcutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new ChooseIconDialog();
                dialog.show(getSupportFragmentManager(), "choose icon dialog");
            }
        });
    }

    private void setShortcutImage(Shortcut shortcut) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(SetActionsActivity.this)
                .load(shortcut.getImageUrl())
                .placeholder(circularProgressDrawable)
                .into(shortcutIcon);
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

    private void clearTitleUpdate(final ImageButton updateTitleButton,
                                  final ImageButton clearTitleButton) {
        clearTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTitleView(v, updateTitleButton, clearTitleButton);
                Toast.makeText(SetActionsActivity.this,
                        "Title update cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTitle(final ImageButton updateTitleButton,
                             final ImageButton clearTitleButton) {
        updateTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentShortcut.setTitle(shortcutTitle.getText().toString());
                appData.fireStoreHandler.updateShortcut(currentShortcut);
                updateTitleView(v, updateTitleButton, clearTitleButton);
                Toast.makeText(SetActionsActivity.this,
                        "Title changed successfully",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTitleView(View v,
                                 ImageButton updateTitleButton,
                                 ImageButton clearTitleButton) {
        shortcutTitle.clearFocus();
        shortcutTitle.setText(currentShortcut.getTitle());
        updateTitleButton.setVisibility(View.INVISIBLE);
        clearTitleButton.setVisibility(View.INVISIBLE);
        hideKeyboard(v);
    }

    private void titleOnFocusChange(final ImageButton updateTitleButton,
                                    final ImageButton clearTitleButton) {
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
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setOnItemLongClickListener(new ActionAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(SetActionsActivity.this, "Malol make me a menu" + position, Toast.LENGTH_SHORT).show();
                lastPosition = position;
                showPopUpMenu(view);
            }
        });

        setSwitchClick();
    }

    protected void showPopUpMenu(View view) {
        PopupMenu popup = new PopupMenu(SetActionsActivity.this, view);
        popup.setOnMenuItemClickListener(SetActionsActivity.this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }


    private void setSwitchClick() {
        adapter.setOnSwitchClickListener(new ActionAdapter.OnSwitchClickListener() {

            private int defaultSwitchSet = 0;

            @Override
            public void onSwitchClick(int position, boolean isChecked) {
                if (defaultSwitchSet < currentShortcut.getActionDataList().size()) {
                    defaultSwitchSet++;
                } else {
                    currentShortcut.getActionDataList().get(position).setIsActivated(isChecked);
                    appData.fireStoreHandler.updateShortcut(currentShortcut);
                }
            }
        });
    }

    private void showAddActionDialog() {
        FloatingActionButton addActionButton = findViewById(R.id.addAction);
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
                        showActionDialog(action, false);
                    }
                });
            }
        });
    }

    protected void showActionDialog(final ActionData actionData, final boolean edit) {
        ActionDialog actionDialog = Objects
                .requireNonNull(ActionFactory
                        .getAction(actionData.getTitle())).getDialog();
        Bundle bundle = new Bundle();
        if (actionDialog != null) {
            final ActionData newAction = new ActionData();
            newAction.copyAction(actionData);
            bundle.putStringArrayList("data", newAction.getData());
            actionDialog.show(getSupportFragmentManager(),
                    newAction.getTitle() + " dialog");
            actionDialog.setNewDialogListener(new ActionDialog.DialogListener() {
                @Override
                public void applyUserInfo(ArrayList<String> data, String description) {
                    newAction.setDescription(description);
                    newAction.copyData(data);
                    addAction(newAction, edit);
                }
            });
        } else {
            addAction(actionData, edit);
        }
        actionDialog.setArguments(bundle);

    }

    private void addAction(ActionData action, final boolean edit) {
        if (edit) {
            editAction(action);
        } else {
            currentShortcut.getActionDataList().add(action);
            adapter.notifyItemInserted(currentShortcut.getActionDataList().size() - 1);

        }
        appData.fireStoreHandler.updateShortcut(currentShortcut);
    }

    private void editAction(ActionData action) {
        currentShortcut.getActionDataList().remove(lastPosition);
        adapter.notifyItemRemoved(lastPosition);
        currentShortcut.getActionDataList().add(lastPosition, action);
        adapter.notifyItemInserted(lastPosition);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper
            .SimpleCallback(ItemTouchHelper.UP
            | ItemTouchHelper.DOWN
            | ItemTouchHelper.START
            | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {

            int fromPos = viewHolder.getAdapterPosition();
            int toPos = target.getAdapterPosition();

            Collections.swap(currentShortcut.getActionDataList(), fromPos, toPos);
            appData.fireStoreHandler.updateShortcut(currentShortcut);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPos, toPos);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };

    @Override
    public void onIconPick(String iconLink) {
        currentShortcut.setImageUrl(iconLink);
        appData.fireStoreHandler.updateShortcut(currentShortcut);
        setShortcutImage(currentShortcut);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        ActionData actionData = currentShortcut.getActionDataList().get(lastPosition);
        switch (item.getItemId()) {
            case R.id.action_popup_edit:
                showActionDialog(actionData, true);
                return true;
            case R.id.action_popup_delete:
                removeAction(actionData);
                return true;
            default:
                return false;
        }
    }

    protected void removeAction(ActionData actionData) {
        currentShortcut.getActionDataList().remove(actionData);
        adapter.notifyDataSetChanged();
        appData.fireStoreHandler.updateShortcut(currentShortcut);
    }
}