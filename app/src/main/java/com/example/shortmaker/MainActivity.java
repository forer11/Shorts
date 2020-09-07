package com.example.shortmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shortmaker.Adapters.DraggableGridAdapter;
import com.example.shortmaker.DataClasses.Shortcut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseMenuActivity {
    List<Shortcut> shortcuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shortcuts = new ArrayList<>();
        shortcuts.add(new Shortcut("yay", ""));
        shortcuts.add(new Shortcut("yay1", ""));
        shortcuts.add(new Shortcut("yay2", ""));
        shortcuts.add(new Shortcut("yay3", ""));
        shortcuts.add(new Shortcut("yay4", ""));
        shortcuts.add(new Shortcut("yay5", ""));
        shortcuts.add(new Shortcut("yay6", ""));
        shortcuts.add(new Shortcut("yay7", ""));
        shortcuts.add(new Shortcut("yay8", ""));
        shortcuts.add(new Shortcut("yay9", ""));
        shortcuts.add(new Shortcut("yay10", ""));
        shortcuts.add(new Shortcut("yay15", ""));
        shortcuts.add(new Shortcut("yay16", ""));
        shortcuts.add(new Shortcut("yay17", ""));
        shortcuts.add(new Shortcut("yay18", ""));
        shortcuts.add(new Shortcut("yay19", ""));
        shortcuts.add(new Shortcut("yay110", ""));
        setRecyclerView();


        setToolbar();
    }

    //TODO - replace to something smarter after we decide on which actions the context menu will have
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        CharSequence title = item.getTitle();
        if ("Delete".equals(title)) {
            //TODO - delete selected item
        } else if ("Change Picture".equals(title)) {
            //TODO - replaace selected item
        }
        return true;
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        DraggableGridAdapter adapter = new DraggableGridAdapter(this, shortcuts);
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
