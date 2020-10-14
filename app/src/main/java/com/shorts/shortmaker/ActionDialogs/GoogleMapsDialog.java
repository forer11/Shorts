package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.shorts.shortmaker.CustomAdapter;
import com.shorts.shortmaker.CustomItem;
import com.shorts.shortmaker.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class GoogleMapsDialog extends ActionDialog {

    private String apiId;
    private Button okButton;
    private String finalName;
    private String finalAddress;
    PlacesClient placesClient;
    private View view;
    private String a;
    private int mode;
    private ArrayList<CustomItem> modes;
    private Place chosenPlace = null;
    private int width = 150;
    private CustomItem lastChosenITem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiId = getString(R.string.maps_id);
        if (!Places.isInitialized()) {
            Places.initialize(Objects.requireNonNull(getActivity()).getApplicationContext(),
                    apiId);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        placesClient = Places.createClient(Objects.requireNonNull(getContext()));

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.autocomplete_fragment);
        Objects.requireNonNull(autocompleteSupportFragment)
                .setPlaceFields(Arrays.asList(Place.Field.ID,
                        Place.Field.LAT_LNG,
                        Place.Field.NAME,
                        Place.Field.ADDRESS,
                        Place.Field.ADDRESS_COMPONENTS));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final String locationName = place.getName();
                final String locationAddress = place.getAddress();
                if (locationName != null) {
                    finalName = locationName;
                    finalAddress = locationAddress;
                    chosenPlace = place;
                    if (!lastChosenITem.getSpinnerItemName().equals("Choose transportation mode")) {
                        okButton.setEnabled(true);
                    }
                } else {
                    okButton.setEnabled(false);
                    Toast.makeText(getContext(),
                            "Place is invalid, try choosing something else",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        final ImageButton imageButton = Objects.requireNonNull(autocompleteSupportFragment.getView())
                .findViewById(R.id.places_autocomplete_clear_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = autocompleteSupportFragment
                        .getView().findViewById(R.id.places_autocomplete_search_input);
                editText.setText("");
                okButton.setEnabled(false);
                finalName = "";
                finalAddress = "";
                imageButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.google_maps_dialog, null);

        initializeDialogViews(builder, view);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.google_maps_gif);
        okButton = view.findViewById(R.id.okButton);
        setModesSpinner(view);
        buildDialog(builder, view, "Where to set Google Maps to", okButton);
    }


    protected void getUserInput() {
        ArrayList<String> results = new ArrayList<>();
        results.add(String.valueOf(chosenPlace.getLatLng().latitude));
        results.add(String.valueOf(chosenPlace.getLatLng().longitude));
        results.add(modes.get(mode).getSpinnerItemName());
        String description = "Navigate to " + finalAddress + " by " + modes.get(mode).getSpinnerItemName();
        listener.applyUserInfo(results, description);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Fragment fragment = (Objects.requireNonNull(getFragmentManager())
                .findFragmentById(R.id.autocomplete_fragment));
        FragmentTransaction ft = Objects.requireNonNull(getActivity())
                .getSupportFragmentManager().beginTransaction();
        ft.remove(Objects.requireNonNull(fragment));
        ft.commit();
    }

    protected void setModesSpinner(View view) {
        modes = new ArrayList<>(Arrays.asList(new CustomItem(
                        "Choose transportation mode", R.drawable.ic_baseline_arrow_drop_down_circle_24),
                new CustomItem("Driving", R.drawable.ic_baseline_drive_eta_24),
                new CustomItem("Bicycling", R.drawable.ic_baseline_directions_bike_24),
                new CustomItem("Two-wheeler", R.drawable.ic_baseline_two_wheeler_24),
                new CustomItem("Walking", R.drawable.ic_baseline_directions_walk_24)));
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        CustomAdapter adapter = new CustomAdapter(getContext(), modes);
        if (spinner != null) {
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        LinearLayout linearLayout = view.findViewById(R.id.customSpinnerItemLayout);
                        width = linearLayout.getWidth();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    spinner.setDropDownWidth(width);
                    CustomItem item = (CustomItem) parent.getSelectedItem();
                    setSpinnerSelectionListener(item);
                    mode = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        // set Adapter
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }


    protected void setSpinnerSelectionListener(CustomItem item) {
        lastChosenITem = item;
        if (item.getSpinnerItemName().equals(modes.get(0))) {
            okButton.setEnabled(false);
        } else {
            if (chosenPlace != null) {
                okButton.setEnabled(true);
            }
        }
    }

}
