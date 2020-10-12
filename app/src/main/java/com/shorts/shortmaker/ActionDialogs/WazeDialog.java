package com.shorts.shortmaker.ActionDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.shorts.shortmaker.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class WazeDialog extends ActionDialog {

    private String apiId;
    private Button okButton;
    PlacesClient placesClient;
    private View view;

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
                        Place.Field.NAME));
//        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                final LatLng latLng = place.getLatLng();
//                if (latLng != null) {
//                    Log.v("placeApi", "place = " + latLng.latitude + ":" + latLng.longitude);
//                }
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//
//            }
//        });

    }

//    private TextWatcher userInputTextWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            if (editTextAddress.getText().toString().equals("")) {
//                editTextAddress.setError("Enter an address");
//                okButton.setEnabled(false);
//            } else {
//                editTextAddress.setError(null);
//                okButton.setEnabled(true);
//            }
//        }
//    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.waze_dialog, null);

        initializeDialogViews(builder, view);
        return builder.create();
    }

    protected void initializeDialogViews(AlertDialog.Builder builder, View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        setDialogImage(imageView, R.drawable.waze_gif);
        okButton = view.findViewById(R.id.okButton);
        buildDialog(builder, view, "Where to set Waze to", okButton);
    }


    protected void getUserInput() {
        String address = "";
        ArrayList<String> results = new ArrayList<>();
        results.add(address);
        String description = "Waze"; //TODO decide
        listener.applyUserInfo(results, description);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.autocomplete_fragment));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}
