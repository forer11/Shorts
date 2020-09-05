package com.example.shortmaker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.shortmaker.SystemHandlers.BluetoothHandler;

public class ActivateBluetoothActivity extends BaseMenuActivity {
    BluetoothHandler bluetoothHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_bluetooth);
        setToolbar();
        bluetoothHandler = new BluetoothHandler(ActivateBluetoothActivity.this);

        Button bluetoothOnButton = findViewById(R.id.bluetooth_on_button);
        bluetoothOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothHandler.enableBluetooth();
            }
        });

        final Button bluetoothOffButton = findViewById(R.id.bluetooth_off_button);
        bluetoothOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothHandler.disableBluetooth();
            }
        });


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

    @Override
    protected void onDestroy() {
        bluetoothHandler.unregister();
        super.onDestroy();
    }
}
