package de.rrsoftware.cellid_tool.ui.locations;

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
import de.rrsoftware.cellid_tool.model.CellLocationManager;
import de.rrsoftware.cellid_tool.ui.camera.CameraViewActivity;

public class RegisterLocationActivity extends AppCompatActivity {
    private static final String LOGTAG = "RegisterActivity";
    public static final String CELL_ID = "CellId";
    private int cellId;
    private CellLocationManager lm;

    @BindView(R.id.cid)
    TextView cidView;

    @BindView(R.id.place)
    TextInputEditText placeView;

    @BindView(R.id.latitude)
    TextInputEditText latitudeView;

    @BindView(R.id.longitude)
    TextInputEditText longitudeView;

    @BindView(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);
        ButterKnife.bind(this);
        lm = CellLocationManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cellId = getIntent().getIntExtra(CELL_ID, 0);
        cidView.setText(String.valueOf(cellId));
        if (lm.isCellKnown(cellId)) {
            if (placeView.getText().length() == 0) {
                placeView.setText(lm.getDescription(cellId));
            }

            latitudeView.setText(String.valueOf(lm.getLatitude(cellId)));
            longitudeView.setText(String.valueOf(lm.getLongitude(cellId)));
        } else {
            final Location gpsLocation = getGPSLocation();
            if (gpsLocation != null) {
                latitudeView.setText(String.valueOf(gpsLocation.getLatitude()));
                longitudeView.setText(String.valueOf(gpsLocation.getLongitude()));
            }
        }

        final File imageFile = new File(getFilesDir(), cellId + ".jpg");
        if (imageFile.exists()) {
            imageView.setImageDrawable(null);
            imageView.setImageURI(Uri.fromFile(imageFile));
        }
    }

    @OnClick(R.id.addPicture)
    void addPicture() {
        final Intent intent = new Intent(this, CameraViewActivity.class);
        intent.putExtra(CameraViewActivity.CELL_ID, cellId);
        startActivity(intent);
    }

    @OnClick(R.id.save)
    void submit() {
        double latitude = 0, longitude = 0;
        try {
            latitude = Double.parseDouble(latitudeView.getText().toString());
            longitude = Double.parseDouble(longitudeView.getText().toString());
        } catch (NumberFormatException e) {
            Log.e(LOGTAG, "failed to parse location");
        }
        lm.addLocation(cellId, placeView.getText().toString(), latitude, longitude);
        CellWidget.sendUpdate(this);
        finish();
    }

    @OnClick(R.id.delete)
    void delete() {
        lm.deleteLocation(cellId);
        CellWidget.sendUpdate(this);
        finish();
    }


    private Location getGPSLocation() {
        final LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

}
