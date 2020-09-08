package com.example.shortmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shortmaker.Adapters.DraggableGridAdapter;
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

import static java.sql.Types.NULL;

public class MainActivity extends BaseMenuActivity implements IconDialog.Callback {
    private static final String ICON_DIALOG_TAG = "icon-dialog";
    public static final int PICKER_REQUEST_CODE = 10;

    List<Shortcut> shortcuts;
    private DraggableGridAdapter adapter;
    int lastPosition = NULL;


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


    //TODO - replace to something smarter after we decide on which actions the context menu will have
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        lastPosition = item.getGroupId();
        String title = item.getTitle().toString();
        if ("Delete".equals(title)) {
            if (lastPosition != NULL) {
                //TODO - delete selected item
                shortcuts.get(lastPosition).setTitle("malol");
                adapter.notifyItemChanged(lastPosition);
            }
        } else if ("Change Icon".equals(title)) {
            showIconPickerDialog();
        } else if ("Load Icon".equals(title)) {
            new GligarPicker().limit(1).requestCode(PICKER_REQUEST_CODE).withActivity(this).show();
        }
        return super.onContextItemSelected(item);
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
            shortcuts.get(lastPosition).setDrawable(drawable);
            adapter.notifyItemChanged(lastPosition);
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
        if (lastPosition != NULL) {
            shortcuts.get(lastPosition).setDrawable(icons.get(0).getDrawable());
            //TODO - change it to changed to one from normal icons
            shortcuts.get(lastPosition).setChanged(true);
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

        adapter.setOnItemClickListener(new DraggableGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,
                        "position = " + position,
                        Toast.LENGTH_SHORT).show();

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
