package com.shorts.shortmaker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shorts.shortmaker.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private SearchView searchView;

    private Circle circle;
    private LatLng mCircleCenter = new LatLng(38.432398, 27.155882);
    private double mCircleRadius = 250;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        setMapFragment();
    }
    private void setMapFragment() {
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
                    Geocoder geocoder = new Geocoder(LocationPickerActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0); //TODO - here is our address
                    mCircleCenter = new LatLng(address.getLatitude(), address.getLongitude());
                    Marker m1 = map.addMarker(new MarkerOptions().position(mCircleCenter).title(location));

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

}