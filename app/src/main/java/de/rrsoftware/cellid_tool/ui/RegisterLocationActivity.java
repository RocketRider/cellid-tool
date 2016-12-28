package de.rrsoftware.cellid_tool.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.service.LocationService;

public class RegisterLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);

        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }
}
