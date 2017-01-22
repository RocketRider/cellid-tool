package de.rrsoftware.cellid_tool.ui;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.camera.CameraActivity;
import de.rrsoftware.cellid_tool.model.CellLocationManager;

public class RegisterLocationActivity extends AppCompatActivity {
    public static final String CELL_ID = "CellId";
    private int cellId;
    private CellLocationManager lm;

    @BindView(R.id.cid)
    TextView cidView;

    @BindView(R.id.place)
    TextInputEditText placeView;

    @BindView(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);
        ButterKnife.bind(this);
        lm = CellLocationManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cellId = getIntent().getIntExtra(CELL_ID, 0);
        if (lm.isCellKnown(cellId)) {
            cidView.setText(String.valueOf(cellId));
            placeView.setText(lm.getDescription(cellId));
        } else {
            Location gpsLocation = getGPSLocation();
            Log.wtf("TEST", gpsLocation.getLatitude() + ", " + gpsLocation.getLongitude());
        }

        File imageFile = new File(getFilesDir(), cellId + ".jpg");
        if (imageFile.exists()) {
            imageView.setImageDrawable(null);
            imageView.setImageURI(Uri.fromFile(imageFile));
        }


    }

    @OnClick(R.id.addPicture)
    void addPicture() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.CELL_ID, cellId);
        startActivity(intent);
    }

    @OnClick(R.id.save)
    void submit() {
        lm.addLocation(cellId, placeView.getText().toString(), 0, 0);
        finish();
    }

    @OnClick(R.id.delete)
    void delete() {
        lm.deleteLocation(cellId);
        finish();
    }


    private Location getGPSLocation() {
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

}
