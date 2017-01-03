package de.rrsoftware.cellid_tool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.service.LocationService;

public class ListLocationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);


        //If the service is not running, it will be started now
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }
}
