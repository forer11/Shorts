package com.shorts.shortmaker.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionFactory;
import com.shorts.shortmaker.Adapters.ActionAdapter;
import com.shorts.shortmaker.AppData;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.DataClasses.Shortcut;
import com.shorts.shortmaker.DialogFragments.ChooseActionDialog;
import com.shorts.shortmaker.DialogFragments.ChooseIconDialog;
import com.shorts.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.shorts.shortmaker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
//

public class SetActionsActivity extends AppCompatActivity implements ChooseIconDialog.OnIconPick,
        PopupMenu.OnMenuItemClickListener,
        OnMapReadyCallback {

    private static final String ICON_DIALOG_TAG = "icon-dialog";
    public static final int NO_POSITION = -1;

    int lastPosition = NO_POSITION;

    private Shortcut currentShortcut;
    AppData appData;
    private EditText shortcutTitle;
    private ActionAdapter adapter;
    private ImageView shortcutIcon;
    private ImageView gifPlaceHolder;
    private TextView noShortcutText;
    private ImageView moreButton;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private SearchView searchView;
    private int radius = 0;
    private CircleOptions circleOptions;
    private Marker m1;
    private Circle circle;
    private LatLng mCircleCenter = new LatLng(38.432398, 27.155882);
    private double mCircleRadius = 250;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_actions);
        setBackButton();
        appData = (AppData) getApplicationContext();
        getShortcutObject();
        showAddActionDialog();
        setGifPlaceHolder();

        setMapFragment();

    }

    private void setMapFragment() {
        noShortcutText = findViewById(R.id.noShortcutsText);
        moreButton = findViewById(R.id.moreButton);

        SeekBar mRadiusSeekBar = (SeekBar)findViewById( R.id.seekbar);
        setSeekBar(mRadiusSeekBar);

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        setSearchView();

        mapFragment.getMapAsync(this);
    }

    private void setSeekBar(SeekBar mRadiusSeekBar) {
        mRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filterMarkers(progress * 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(SetActionsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0); //TODO - here is our address 
                    mCircleCenter = new LatLng(address.getLatitude(), address.getLongitude());
                    m1 = map.addMarker(new MarkerOptions().position(mCircleCenter).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mCircleCenter, 15));

                    drawCircle();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private List<LatLng> mPoints = new ArrayList<>();
    private List<Marker> mMarkers = new ArrayList<>();

    private void filterMarkers(double radiusForCircle){
        circle.setRadius(radiusForCircle);
        float[] distance = new float[2];
        for(int m = 0; m < mMarkers.size(); m++){
            Marker marker = mMarkers.get(m);
            LatLng position = marker.getPosition();
            double lat = position.latitude;
            double lon = position.longitude;

            Location.distanceBetween(lat, lon, mCircleCenter.latitude,
                    mCircleCenter.longitude, distance);

            boolean inCircle = distance[0] <= radiusForCircle;
            marker.setVisible(inCircle);
        }
    }

    protected void drawCircle() {
        circle = map.addCircle(new CircleOptions()
                .strokeWidth(4)
                .radius(mCircleRadius)
                .center(mCircleCenter)
                .strokeColor(Color.parseColor("#D1C4E9"))
                .fillColor(Color.parseColor("#657C4DFF")));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker")); //TODO - put our location here
        map.moveCamera(CameraUpdateFactory
                .newLatLngZoom(mCircleCenter, 12));

        drawCircle();
    }

    private void setGifPlaceHolder() {
        gifPlaceHolder = findViewById(R.id.gifPlaceHolder);
        Glide.with(SetActionsActivity.this).load(R.drawable.empty_gif)
                .transition(withCrossFade())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(gifPlaceHolder);
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
        if (currentShortcut.getActionDataList().isEmpty()) {
            gifPlaceHolder.setVisibility(View.VISIBLE);
            noShortcutText.setVisibility(View.VISIBLE);
        }
        adapter.setOnMoreClickListener(new ActionAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClickListener(View view, int position) {
                lastPosition = position;
                showMoreMenu(view);
            }
        });
        adapter.setOnConditionsClickListener(new ActionAdapter.OnConditionsClickListener() {
            @Override
            public void onConditionsClickListener(View view, int position) {
                lastPosition = position;
                showConditionsMenu(view);
            }
        });
        //TODO - need it for swipe?
//        adapter.setOnItemLongClickListener(new ActionAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });

        setSwitchClick();
    }

    protected void showMoreMenu(View view) {
        PopupMenu popup = new PopupMenu(SetActionsActivity.this, view);
        popup.setOnMenuItemClickListener(SetActionsActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true);
        }
        popup.inflate(R.menu.more_menu);
        popup.show();
    }

    protected void showConditionsMenu(View view) {
        PopupMenu popup = new PopupMenu(SetActionsActivity.this, view);
        popup.setOnMenuItemClickListener(SetActionsActivity.this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true);
        }
        popup.inflate(R.menu.conditions_menu);
        popup.show();
    }

    private void setSwitchClick() {
        adapter.setOnSwitchClickListener(new ActionAdapter.OnSwitchClickListener() {
            @Override
            public void onSwitchClick(int position, boolean isChecked) {
                currentShortcut.getActionDataList().get(position).setIsActivated(isChecked);
                appData.fireStoreHandler.updateShortcut(currentShortcut);
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
        gifPlaceHolder.setVisibility(View.INVISIBLE);
        noShortcutText.setVisibility(View.INVISIBLE);
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
        if (item.getGroupId() == R.id.more_menu) {
            return onMoreMenuItemClick(item, actionData);
        } else if (item.getGroupId() == R.id.conditions_menu) {
            return onConditionMenuItemClick(item, actionData, lastPosition);
        } else {
            return false;
        }
    }

    protected boolean onMoreMenuItemClick(MenuItem item, ActionData actionData) {
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

    protected boolean onConditionMenuItemClick(MenuItem item,
                                               ActionData actionData,
                                               int position) {
        String conditionDescription;
        switch (item.getItemId()) {
            case R.id.onShortcutClick:
                conditionDescription = ActionFactory.WHEN_CLICKING_ON_A_SHORTCUT;
                if (!actionData.getConditionDescription().equals(conditionDescription)) {
                    actionData.setConditionDescription(conditionDescription);
                    actionData.setCondition(ActionFactory.Conditions.ON_DEFAULT);
                    adapter.notifyDataSetChanged();
                    //TOOD- update in firestore

                }
                return true;
            case R.id.whenEnteringLocation:


                //TODO - call a special map dialog
                conditionDescription = ActionFactory.ON_LOCATION; //TODO - here it will be the location we got from the dialog
                if (!actionData.getConditionDescription().equals(conditionDescription)) {
                    actionData.setConditionDescription(conditionDescription);
                    actionData.setCondition(ActionFactory.Conditions.ON_AT_LOCATION);
                    adapter.notifyDataSetChanged();
                    //TOOD - update in firestore
                }
                return true;
            default:
                return false;
        }
    }

    protected void removeAction(ActionData actionData) {
        currentShortcut.getActionDataList().remove(actionData);
        adapter.notifyDataSetChanged();
        appData.fireStoreHandler.updateShortcut(currentShortcut);
        if (currentShortcut.getActionDataList().isEmpty()) {
            gifPlaceHolder.setVisibility(View.VISIBLE);
            noShortcutText.setVisibility(View.VISIBLE);
        }
    }

}