package com.example.shortmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.shortmaker.Adapters.DraggableGridAdapter;
import com.example.shortmaker.DataClasses.Shortcut;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;
import com.opensooq.supernova.gligar.GligarPicker;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends BaseMenuActivity implements IconDialog.Callback,
        PopupMenu.OnMenuItemClickListener {
    private static final String ICON_DIALOG_TAG = "icon-dialog";
    public static final int PICKER_REQUEST_CODE = 10;
    public static final int NO_POSITION = -1;
    public static final int KAWAII_ICON_CATEGORY = 202020;
    public static final int PUSHEEN_ICON_CATEGORY = 303030;

    List<Shortcut> shortcuts;
    private DraggableGridAdapter adapter;
    int lastPosition = NO_POSITION;


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

    }



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
                if (shortcuts.get(position).getTitle().equals("Driving")) {
                    drivingConfiguration();
                }

            }
        });
    }

    private void drivingConfiguration() {
        //TODO - add more actions when driving
        openSpotify();
        openWaze();
//        putPhoneOnVibrateMode();
//        putPhoneOnRingingMode();
//        changePhoneSoundMode();
//        sendTextMessage(true);
    }

    private void sendTextMessage(boolean sendThroughWhatsapp) {
        Intent sendIntent = new Intent();
        if(sendThroughWhatsapp){
            sendIntent.setPackage("com.whatsapp");
        }
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send."); //TODO - change to a costumized user text
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void changePhoneSoundMode() {
        //TODO - add a picker between silent/normal/vibration
        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        //For Silent mode
        putPhoneOnSilent(audioManager);
        //For Normal mode
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        //For Vibrate mode
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

    private void putPhoneOnSilent(AudioManager audioManager) {
        NotificationManager notificationManager =
                (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }


    private void openWaze() {
        try {
            // Launch Waze to look for Hawaii:.
            // we can also give the following uri :  "https://waze.com/ul?ll=40.761043,-73.980545&navigate=yes" for it to navigate automatically
            String url = "https://waze.com/ul?q=Hawaii"; //TODO - change to user defined address
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }

    private void openSpotify() {
        boolean isSpotifyInstalled = checkIfSpotifyInstalled();
        if (isSpotifyInstalled) {
            launchSpotify();
        } else {
            installSpotify();
        }
    }

    private void installSpotify() {
        final String appPackageName = "com.spotify.music";
        final String referrer = "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";
        try {
            Uri uri = Uri.parse("market://details")
                    .buildUpon()
                    .appendQueryParameter("id", appPackageName)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException ignored) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                    .buildUpon()
                    .appendQueryParameter("id", appPackageName)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    private void launchSpotify() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //TODO - we can generally open spotify with "spotify:open" and not a specific album
        // we can delete the ":play: from the uri in order for the song not to be played automatically
        intent.setData(Uri.parse("spotify:album:0sNOF9WDwhWunNAHPD3Baj:play"));
        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + MainActivity.this.getPackageName()));
        startActivity(intent);
    }

    private boolean checkIfSpotifyInstalled() {
        PackageManager pm = getPackageManager();
        boolean isSpotifyInstalled;
        try {
            pm.getPackageInfo("com.spotify.music", 0);
            isSpotifyInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isSpotifyInstalled = false;
        }
        return isSpotifyInstalled;
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

}
