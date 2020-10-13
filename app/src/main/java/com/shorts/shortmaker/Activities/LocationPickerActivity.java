package com.shorts.shortmaker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.DialogFragments.ChooseActionDialog;
import com.shorts.shortmaker.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private Circle circle;
    private LatLng mCircleCenter = new LatLng(38.432398, 27.155882);
    private double mCircleRadius = 250;
    private String locationName;
    private String locationAddress;
    private Address address;
    private LatLng latLng;
    private FloatingActionButton setAddressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        String apiId = getString(R.string.maps_id);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),
                    apiId);
        }
        setMapFragment();
        setBackButton();

        setAddressButton = findViewById(R.id.setAddressButton);
        setAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SetActionsActivity.class);
                intent.putExtra("address", locationAddress);
                intent.putExtra("name", locationName);
                intent.putExtra("latitude", latLng.latitude);
                intent.putExtra("longtitude", latLng.longitude);
                intent.putExtra("radius", mCircleRadius);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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

    private void setMapFragment() {
        SeekBar mRadiusSeekBar = (SeekBar) findViewById(R.id.seekbar);
        setSeekBar(mRadiusSeekBar);

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
        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager()
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
                setAddressButton.setVisibility(View.VISIBLE);
                latLng = place.getLatLng();
                locationName = place.getName();
                locationAddress = place.getAddress();
                if (locationName != null && latLng != null) {
                    mCircleCenter = new LatLng(latLng.latitude, latLng.longitude);
                    Marker m1 = map.addMarker(new MarkerOptions().position(mCircleCenter).title(locationName));

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mCircleCenter, 15));
                    drawCircle();
                }
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
//        final ImageButton imageButton = Objects.requireNonNull(autocompleteSupportFragment.getView())
//                .findViewById(R.id.places_autocomplete_clear_button);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText editText = autocompleteSupportFragment
//                        .getView().findViewById(R.id.places_autocomplete_search_input);
//                editText.setText("");
//                okButton.setEnabled(false);
//                finalName = "";
//                finalAddress = "";
//                imageButton.setVisibility(View.INVISIBLE);
//            }
//        });


    }

    private List<LatLng> mPoints = new ArrayList<>();
    private List<Marker> mMarkers = new ArrayList<>();

    private void filterMarkers(double radiusForCircle) {
        circle.setRadius(radiusForCircle);
        float[] distance = new float[2];
        for (int m = 0; m < mMarkers.size(); m++) {
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

}