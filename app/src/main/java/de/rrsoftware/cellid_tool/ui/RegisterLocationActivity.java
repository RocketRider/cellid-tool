package de.rrsoftware.cellid_tool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.camera.CameraActivity;
import de.rrsoftware.cellid_tool.model.LocationManager;

public class RegisterLocationActivity extends AppCompatActivity {
    public static final String CELL_ID = "CellId";
    private int cellId;
    private LocationManager lm;

    @BindView(R.id.cid)
    TextView cidView;

    @BindView(R.id.place)
    TextInputEditText placeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);
        ButterKnife.bind(this);
        lm = LocationManager.getInstance(this);

        cellId = getIntent().getIntExtra(CELL_ID, 0);
        cidView.setText(String.valueOf(cellId));
        placeView.setText(lm.getDescription(cellId));
    }

    @OnClick(R.id.addPicture)
    void addPicture() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.save)
    void submit() {
        lm.addLocation(cellId, placeView.getText().toString());
        finish();
    }

    @OnClick(R.id.delete)
    void delete() {
        lm.deleteLocation(cellId);
        finish();
    }
}
