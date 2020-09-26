package com.example.shortmaker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.example.shortmaker.Adapters.DraggableGridAdapter;
import com.example.shortmaker.AppData;
import com.example.shortmaker.DialogFragments.ChooseIconDialog;
import com.example.shortmaker.DataClasses.Shortcut;
import com.example.shortmaker.R;
import com.example.shortmaker.Views.MovableFloatingActionButton;
import com.opensooq.supernova.gligar.GligarPicker;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainActivity extends BaseMenuActivity implements PopupMenu.OnMenuItemClickListener,
        ChooseIconDialog.OnIconPick {
    private static final String ICON_DIALOG_TAG = "icon-dialog";
    private static final int REQUEST_CALL = 1;
    public static final String PHONE_CALL_DIALOG_TITLE = "Make action phone call";
    public static final String PHONE_CALL_DIALOG_POS_BTN = "DIAL";
    public static final int PICKER_REQUEST_CODE = 10;
    public static final int NO_POSITION = -1;
    public static final int KAWAII_ICON_CATEGORY = 202020;
    public static final int PUSHEEN_ICON_CATEGORY = 303030;

    public static final String DRIVING_CONFIGURATION = "Driving";

    List<Shortcut> shortcuts;
    private DraggableGridAdapter adapter;
    int lastPosition = NO_POSITION;
    private EditText editText;
    private View phoneCallDialogLayout;
    private AlertDialog makeCallDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shortcuts = new ArrayList<>();
        shortcuts.add(new Shortcut("Sport",
                "https://firebasestorage.googleapis.com/v0/b/shortmaker-dbb76.appspot.com/o/icons%2Fcooking.png?alt=media&token=1d91c224-ee6d-421b-87ae-8cc7427a26f1",
                false));
        shortcuts.add(new Shortcut("Study", "https://firebasestorage.googleapis.com/v0/b/shortmaker-dbb76.appspot.com/o/icons%2Fcooking.png?alt=media&token=1d91c224-ee6d-421b-87ae-8cc7427a26f1", false));
        shortcuts.add(new Shortcut("Driving", "https://firebasestorage.googleapis.com/v0/b/shortmaker-dbb76.appspot.com/o/icons%2Fcooking.png?alt=media&token=1d91c224-ee6d-421b-87ae-8cc7427a26f1", false));
        shortcuts.add(new Shortcut("Party", "https://firebasestorage.googleapis.com/v0/b/shortmaker-dbb76.appspot.com/o/icons%2Fcooking.png?alt=media&token=1d91c224-ee6d-421b-87ae-8cc7427a26f1", false));
        shortcuts.add(new Shortcut("Cooking", "https://firebasestorage.googleapis.com/v0/b/shortmaker-dbb76.appspot.com/o/icons%2Fcooking.png?alt=media&token=1d91c224-ee6d-421b-87ae-8cc7427a26f1", false));
        setRecyclerView();

        setToolbar();

        MovableFloatingActionButton addShortcut = findViewById(R.id.addShortcut);
        addShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateShortcutDialog();
            }
        });
//        setMakeCallDialog();
    }


//    private void setMakeCallDialog() {
//        phoneCallDialogLayout = getLayoutInflater().inflate(R.layout.phone_call_dialog_layout, null);
//        editText = phoneCallDialogLayout.findViewById(R.id.edit_text_number);
//        showAlertDialogMakeCall();
//    }
//
//
//    public void showAlertDialogMakeCall() {
//        // Create an alert builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(PHONE_CALL_DIALOG_TITLE);
//        // set the custom layout
//        builder.setView(phoneCallDialogLayout);
//        // add action button
//        builder.setPositiveButton(PHONE_CALL_DIALOG_POS_BTN, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // send data from the AlertDialog to the Activity
//                makePhoneCall(editText.getText().toString());
//            }
//        }).setIcon(R.drawable.ic_phone);
//        // create and show the alert dialog
//        makeCallDialog = builder.create();
//    }
//
//
//    private void makePhoneCall(String number) {
//        if (number.trim().length() > 0) {
//            if (ContextCompat.checkSelfPermission(MainActivity.this,
//                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
//            } else {
//                String dial = "tel:" + number;
//                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
//            }
//        } else {
//            Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CALL) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                makePhoneCall(editText.getText().toString());
//            } else {
//                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == PICKER_REQUEST_CODE) {
//            String[] pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT); // action list of length 1
//            Drawable drawable = Drawable.createFromPath(pathsList[0]);
//            if (lastPosition != NO_POSITION) {
//                shortcuts.get(lastPosition).setImageUrl(drawable);
//                adapter.notifyItemChanged(lastPosition);
//            }
        }
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new DraggableGridAdapter(this, shortcuts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
        setOnItemClickListener();
        setOnItemLongClickListener();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setOnItemLongClickListener() {
        adapter.setOnItemLongClickListener(new DraggableGridAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                lastPosition = position;
                showPopupMenu(view);
            }
        });
    }

    private void setOnItemClickListener() {
        adapter.setOnItemClickListener(new DraggableGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,
                        "position = " + position,
                        Toast.LENGTH_SHORT).show();
                AppData appData = (AppData) getApplicationContext();
                System.out.println("yay");
            }
        });
    }

    private void showCreateShortcutDialog() {
        final FlatDialog flatDialog = new FlatDialog(MainActivity.this);
        flatDialog.setTitle("Add Shortcut")
                .setIcon(R.drawable.shortcut)
                .setSubtitle("Choose the shortcut name")
                .setFirstTextFieldHint("shortcut name")
                .setFirstButtonText("OK")
                .setSecondButtonText("CANCEL")
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String shortcutName = flatDialog.getFirstTextField();
                        if (shortcutName.equals("")) {
                            Toast.makeText(MainActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getBaseContext(), SetActionsActivity.class);
                            intent.putExtra("shortcutName", shortcutName);
                            startActivity(intent);

                            //TODO - add to the grid the new shortcut
                        }
                    }
                })
                .withSecondButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flatDialog.dismiss();
                    }
                })
                .show();
    }


    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popup_edit:
                //TODO - edit selected item
                return true;
            case R.id.action_popup_delete:
                //TODO - delete selected item
                shortcuts.get(lastPosition).setTitle("malol");
                return true;
            case R.id.action_popup_change_icon:
                DialogFragment dialog = new ChooseIconDialog();
                dialog.show(getSupportFragmentManager(), "choose action dialog");
                return true;
            case R.id.action_popup_load_icon:
                new GligarPicker().limit(1).requestCode(PICKER_REQUEST_CODE).withActivity(this).show();
                return true;
            default:
                return false;
        }
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ShortMaker");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setProfile();

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();

        setSearchQueryTextListener(searchView);
        return true;
    }

    private void setSearchQueryTextListener(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper
            .SimpleCallback(ItemTouchHelper.RIGHT
            | ItemTouchHelper.LEFT
            | ItemTouchHelper.UP
            | ItemTouchHelper.DOWN
            | ItemTouchHelper.START
            | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            int fromPos = viewHolder.getAdapterPosition();
            int toPos = target.getAdapterPosition();

            Collections.swap(shortcuts, fromPos, toPos);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPos, toPos);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };


    @Override
    public void onIconPick(String iconLink) {
        shortcuts.get(lastPosition).setImageUrl(iconLink);
        adapter.notifyItemChanged(lastPosition);
    }
}
