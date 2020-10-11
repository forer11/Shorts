package com.shorts.shortmaker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;

import android.content.Intent;

import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionFactory;
import com.shorts.shortmaker.Actions.Action;
import com.shorts.shortmaker.Adapters.DraggableGridAdapter;
import com.shorts.shortmaker.AppData;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.DialogFragments.ChooseIconDialog;
import com.shorts.shortmaker.DataClasses.Shortcut;
import com.shorts.shortmaker.DialogFragments.CreateShortcutDialog;
import com.shorts.shortmaker.DialogFragments.DeleteShortcutDialog;
import com.shorts.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.shorts.shortmaker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MainActivity extends BaseMenuActivity implements ChooseIconDialog.OnIconPick {

    public static final int PICKER_REQUEST_CODE = 10;
    public static final int NO_POSITION = -1;
    public static final int Y = 1;
    public static final int X = 0;
    public static final String TAG = "location";


    List<Shortcut> shortcuts;
    List<Shortcut> fullShortcutsList;
    private DraggableGridAdapter adapter;
    int lastPosition = NO_POSITION;

    AppData appData;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView recyclerView = null;
    private String searchText = "";
    private PopupWindow popupWindow;
    private int screenWidth;
    private int screenHeight;
    private ImageView gifPlaceHolder;
    private TextView noShortcutText;


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenSize();

        setObjects();
        setToolbar();
        setAddShortcutButton();
//        Button button = findViewById(R.id.button2);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ActivateShortcutActivity.class).setAction(Intent.ACTION_MAIN);
//                intent.putExtra("shortcutId", shortcuts.get(0).getId());
//                if (ShortcutManagerCompat.isRequestPinShortcutSupported(getApplicationContext())) {
//                    ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(getApplicationContext(), "#1")
//                            .setIntent(intent)
//                            .setShortLabel("Test")
//                            .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.richi))
//                            .build();
//                    ShortcutManagerCompat.requestPinShortcut(getApplicationContext(), shortcutInfo, null);
//                } else {
//                    Toast.makeText(MainActivity.this,"launcher does not support short cut icon",Toast.LENGTH_LONG).show();
//                }
//            }
//        });

//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), "your api key");
//        }
//
//// Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });
    }


    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private void setObjects() {
        shortcuts = new ArrayList<>();
        fullShortcutsList = new ArrayList<>();
        adapter = null;
        appData = (AppData) getApplicationContext();
        setGifPlaceHolder();
        noShortcutText = findViewById(R.id.noShortcutsText);
    }

    private void setGifPlaceHolder() {
        gifPlaceHolder = findViewById(R.id.gifPlaceHolder);
        Glide.with(MainActivity.this).load(R.drawable.empty_gif)
                .transition(withCrossFade())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(gifPlaceHolder);
    }

    private void getUserDataAndLoadRecyclerview() {
        shortcuts.clear();
        appData.fireStoreHandler.setUserKey(appData.fireBaseAuthHandler.user);
        appData.fireStoreHandler.loadShortcuts(new FireStoreHandler.FireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<Shortcut> shortcutsList, Boolean success) {
                if (success) {
                    shortcuts.addAll(shortcutsList);
                    fullShortcutsList.clear();
                    fullShortcutsList.addAll(shortcuts);
                    if (fullShortcutsList.isEmpty()) {
                        gifPlaceHolder.setVisibility(View.VISIBLE);
                        noShortcutText.setVisibility(View.VISIBLE);
                    }
                    if (recyclerView != null) {
                        filterShortcuts(searchText);
                    }
                    setNewPositions();
                    setRecyclerView();
                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setNewPositions() {
        int i = 0;
        for (Shortcut shortcut : shortcuts) {
            shortcut.setPos(i++);
            appData.fireStoreHandler.updateShortcut(shortcut);
        }
    }

    private void setAddShortcutButton() {
        FloatingActionButton addShortcut = findViewById(R.id.addShortcut);
        addShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateShortcutDialog();
            }
        });
    }


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
        recyclerView = findViewById(R.id.recyclerView);
        if (adapter == null) {
            adapter = new DraggableGridAdapter(this, shortcuts, fullShortcutsList);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(adapter);
            setOnItemClickListener();
            setOnItemLongClickListener();
            itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        } else {
            adapter.notifyDataSetChanged();
        }
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
                Shortcut shortcut = shortcuts.get(position);
                for (ActionData actionData : shortcut.getActionDataList()) {
                    if (actionData.getIsActivated()) {
                        Action action = ActionFactory.getAction(actionData.getTitle());
                        if (action != null) {
                            action.setData(actionData.getData());
                            action.activate(getBaseContext(), MainActivity.this);
                        }
                    }
                }
                Toast.makeText(MainActivity.this,
                        "position = " + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCreateShortcutDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateShortcutDialog();
        Bundle args = new Bundle();
        args.putInt("pos", shortcuts.size());
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "choose action dialog");
    }


    private void showPopupMenu(View view) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.popup_menu_horrizental, null);

        LinearLayout editLayout, deleteLayout, copyLayout, swapIconLayout;
        editLayout = customView.findViewById(R.id.edit_layout);
        deleteLayout = customView.findViewById(R.id.delete_layout);
        copyLayout = customView.findViewById(R.id.copy_layout);
        swapIconLayout = customView.findViewById(R.id.swap_icon_layout);

        setHorizontalMenuClickEvents(editLayout, deleteLayout, copyLayout, swapIconLayout);

        popupWindow = new PopupWindow(customView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        setPopupLocation(view, customView);
    }

    private void setPopupLocation(View view, View costumeView) {
        int[] location = {0, 0};
        view.getLocationOnScreen(location);
        if (location[Y] >= (screenHeight / 2)) {
            int h = -view.getHeight();
            popupWindow.showAsDropDown(view, 0, (int) Math.round(h * 1.71));
            setTriangleLocation(location, true, costumeView);
        } else {
            popupWindow.showAsDropDown(view);
            setTriangleLocation(location, false, costumeView);
        }
    }

    /**
     * sets the traingle location
     *
     * @param location the popup location
     * @param down     indicating if the triangle shuld be facing down or up
     */
    private void setTriangleLocation(int[] location, boolean down, View costumeView) {
        ImageView triangle;
        if (location[X] > screenWidth / 1.5) {
            if (down) { //right bottom
                triangle = costumeView.findViewById(R.id.triangle_bottom_right);
            } else { // right top
                triangle = costumeView.findViewById(R.id.triangle_top_right);
            }

        } else { // left
            if (down) { //left bottom
                triangle = costumeView.findViewById(R.id.triangle_bottom_left);
            } else { //left top
                triangle = costumeView.findViewById(R.id.triangle_top_left);
            }
        }
        triangle.setVisibility(View.VISIBLE);
    }

    private void setHorizontalMenuClickEvents(LinearLayout editLayout, LinearLayout deleteLayout, LinearLayout copyLayout, LinearLayout swapIconLayout) {
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        SetActionsActivity.class);
                intent.putExtra("shortcutId", shortcuts.get(lastPosition).getId());
                startActivity(intent);
                popupWindow.dismiss();
            }
        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionDialog deleteShortcutDialog = new DeleteShortcutDialog();
                deleteShortcutDialog.show(getSupportFragmentManager(),
                        "Delete shortcut dialog dialog");
                deleteShortcutDialog.setNewOnClickListener(new ActionDialog.OnClickListener() {
                    @Override
                    public void onClick() {
                        Shortcut shortcut = shortcuts.get(lastPosition);
                        appData.fireStoreHandler.deleteShortcut(shortcut.getId(),
                                shortcut.getTitle());
                        getUserDataAndLoadRecyclerview();
                    }
                });
                popupWindow.dismiss();
            }
        });

        copyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shortcut shortcut = shortcuts.get(lastPosition);
                Shortcut newShortcut = new Shortcut();
                newShortcut.copyShortcut(shortcut, shortcuts.size());
                appData.fireStoreHandler.addShortcut(newShortcut,
                        new FireStoreHandler.SingleShortcutCallback() {
                            @Override
                            public void onAddedShortcut(String id,
                                                        Shortcut shortcut1,
                                                        Boolean success) {
                                if (success) { //TODO if not successful
                                    getUserDataAndLoadRecyclerview();
                                }
                            }
                        });
                popupWindow.dismiss();
            }
        });

        swapIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new ChooseIconDialog();
                dialog.show(getSupportFragmentManager(), "choose icon dialog");
                popupWindow.dismiss();
                //new GligarPicker().limit(1).requestCode(PICKER_REQUEST_CODE).withActivity(this).show(); (icon load from camera)
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Shorts");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon);// set drawable icon
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                searchText = newText;
                filterShortcuts(newText);
                return false;
            }
        });
    }

    private void filterShortcuts(String newText) {
        adapter.getFilter().filter(newText);
        if (newText.equals("")) {
            itemTouchHelper.attachToRecyclerView(recyclerView);
        } else {
            itemTouchHelper.attachToRecyclerView(null);
        }
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

            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            int fromPos = viewHolder.getAdapterPosition();
            int toPos = target.getAdapterPosition();
            swapShortcutPos(fromPos, toPos);

            Collections.swap(shortcuts, fromPos, toPos);
            Collections.swap(fullShortcutsList, fromPos, toPos);
            moveViews(recyclerView, fromPos, toPos);

            return false;
        }

        private void swapShortcutPos(int fromPos, int toPos) {
            Shortcut fromShortcut = shortcuts.get(fromPos);
            Shortcut toShortcut = shortcuts.get(toPos);

            int tempPos = fromShortcut.getPos();
            fromShortcut.setPos(toShortcut.getPos());
            toShortcut.setPos(tempPos);
            appData.fireStoreHandler.updateShortcut(fromShortcut);
            appData.fireStoreHandler.updateShortcut(toShortcut);
        }

        private void moveViews(@NonNull RecyclerView recyclerView, int fromPos, int toPos) {
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPos, toPos);
            if (Math.abs(toPos - fromPos) > 1) {
                if (toPos < fromPos) {
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(toPos + 1, fromPos);
                } else {
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(toPos - 1, fromPos);
                }
            }
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };


    @Override
    public void onIconPick(String iconLink) {
        Shortcut shortcut = shortcuts.get(lastPosition);
        shortcut.setImageUrl(iconLink);
        adapter.notifyItemChanged(lastPosition);
        appData.fireStoreHandler.updateShortcut(shortcut);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gifPlaceHolder.setVisibility(View.INVISIBLE);
        noShortcutText.setVisibility(View.INVISIBLE);
        getUserDataAndLoadRecyclerview();
    }
}
