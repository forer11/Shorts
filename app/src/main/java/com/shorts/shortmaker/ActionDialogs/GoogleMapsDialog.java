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
import com.shorts.shortmaker.R;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String[] modes;
    private Place chosenPlace = null;

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
        results.add(modes[mode]);
        String description = "Navigate to " + finalAddress + " by " + modes[mode];
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
        modes = new String[]{"Choose transportation mode", "Driving", "Bicycling", "Two-wheeler", "Walking"};
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, modes);
        // set Adapter
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinner(spinner);
    }

    private void setSpinner(final Spinner spinner) {
        //Register a callback to be invoked when an item in this AdapterView has been selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                setSpinnerSelectionListener(position, spinner);
                mode = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    protected void setSpinnerSelectionListener(int position, Spinner spinner) {
        String item = (String) spinner.getItemAtPosition(position);
        if (item.equals(modes[0])){
            okButton.setEnabled(false);
        } else {
            if(chosenPlace!=null){
                okButton.setEnabled(true);
            }
        }
    }


}
