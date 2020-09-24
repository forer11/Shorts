package com.example.shortmaker;

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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.WazeDialog;
import com.example.shortmaker.Actions.ActionSpotify;
import com.example.shortmaker.Actions.ActionWaze;
import com.example.shortmaker.Adapters.DraggableGridAdapter;
import com.example.shortmaker.DataClasses.Action;
import com.example.shortmaker.DataClasses.Shortcut;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;
import com.opensooq.supernova.gligar.GligarPicker;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainActivity extends BaseMenuActivity implements IconDialog.Callback,
        PopupMenu.OnMenuItemClickListener, WazeDialog.WazeDialogListener {
    private static final String ICON_DIALOG_TAG = "icon-dialog";
    private static final int REQUEST_CALL = 1;
    public static final String PHONE_CALL_DIALOG_TITLE = "Make a phone call";
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
        shortcuts.add(new Shortcut("Sport", getDrawable(R.drawable.sport), false));
        shortcuts.add(new Shortcut("Study", getDrawable(R.drawable.study), false));
        shortcuts.add(new Shortcut("Driving", getDrawable(R.drawable.drive_home), false));
        shortcuts.add(new Shortcut("Party", getDrawable(R.drawable.party), false));
        shortcuts.add(new Shortcut("Cooking", getDrawable(R.drawable.cooking), false));
        shortcuts.add(new Shortcut("Sleeping", getDrawable(R.drawable.sleeping), false));
        shortcuts.add(new Shortcut("Relaxing", getDrawable(R.drawable.relax_kawaii), false));
        shortcuts.add(new Shortcut("Meeting", getDrawable(R.drawable.meeting), false));
        shortcuts.add(new Shortcut("Gaming", getDrawable(R.drawable.game), false));
        shortcuts.add(new Shortcut("yay2", getDrawable(R.drawable.richi), false));
        shortcuts.add(new Shortcut("yay3", getDrawable(R.drawable.richi), false));
        shortcuts.add(new Shortcut("yay4", getDrawable(R.drawable.richi), false));
        shortcuts.add(new Shortcut("yay5", getDrawable(R.drawable.richi), false));
        shortcuts.add(new Shortcut("yay6", getDrawable(R.drawable.richi), false));
        shortcuts.add(new Shortcut("yay7", getDrawable(R.drawable.richi), false));
        shortcuts.add(new Shortcut("yay8", getDrawable(R.drawable.richi), false));
        shortcuts.add(new Shortcut("yay9", getDrawable(R.drawable.richi), false));

        setRecyclerView();

        setToolbar();

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
//        // add a button
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
            String[] pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT); // a list of length 1
            Drawable drawable = Drawable.createFromPath(pathsList[0]);
            if (lastPosition != NO_POSITION) {
                shortcuts.get(lastPosition).setDrawable(drawable);
                adapter.notifyItemChanged(lastPosition);
            }
        }
    }


    private void showIconPickerDialog() {
        // If dialog is already added to fragment manager, get it. If not, create a new instance.
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
        if (lastPosition != NO_POSITION) {
            shortcuts.get(lastPosition).setDrawable(icons.get(0).getDrawable());
            boolean isSpecialIcon = icons.get(0).getCategoryId() == KAWAII_ICON_CATEGORY ||
                    icons.get(0).getCategoryId() == PUSHEEN_ICON_CATEGORY;
            //We want to add white tint to all regular icons
            shortcuts.get(lastPosition).setTintNeeded(!isSpecialIcon);
            adapter.notifyItemChanged(lastPosition);
        }
    }


    @Override
    public void onIconDialogCancelled() {

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
                if (shortcuts.get(position).getTitle().equals(DRIVING_CONFIGURATION)) {
                    drivingConfiguration();
                }

            }
        });
    }

    private void drivingConfiguration() {
        //TODO - add more actions when driving
//        Action spotify = new ActionSpotify(this);
//        DialogFragment dialogFragment = spotify.getDialog(); //TOOD - todo in interface
//        if(dialogFragment!=null) {
//            dialogFragment.show(getSupportFragmentManager(), "waze dialog");
//        }

//        spotify.activate();

        ActionWaze waze = new ActionWaze(this);
        DialogFragment dialogFragment = waze.getDialog(); //TOOD - todo in interface
        if(dialogFragment!=null) {
            dialogFragment.show(getSupportFragmentManager(), "waze dialog");
        }


//        waze.activate();

//        Action soundMode = new ActionSoundSettings(this,0);
//        soundMode.activate();

//        Action textMessage = new ActionSendTextMessage(this,true);
//        textMessage.activate();

//        makeCallDialog.show();
//
//        Action alarmClock = new ActionAlarmClock(this);
//        alarmClock.activate();


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
                adapter.notifyItemChanged(lastPosition);
                return true;
            case R.id.action_popup_change_icon:
                showIconPickerDialog();
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
    public void applyText(String address) {

    }
}
