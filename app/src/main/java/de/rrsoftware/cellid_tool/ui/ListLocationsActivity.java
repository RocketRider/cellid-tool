package de.rrsoftware.cellid_tool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.model.LocationManager;
import de.rrsoftware.cellid_tool.service.LocationService;

public class ListLocationsActivity extends AppCompatActivity {
    @BindView(R.id.list)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);
        ButterKnife.bind(this);

        //If the service is not running, it will be started now
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        LocationManager lm = LocationManager.getInstance(this);
        listView.setAdapter(new LocationListAdapter(this, lm.getAllCellIds()));
    }
}