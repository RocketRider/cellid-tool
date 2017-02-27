package de.rrsoftware.cellid_tool.ui.permissions;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.ui.locations.ListLocationsActivity;

public class PermissionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        askForPermissions();
    }

    private void askForPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(final MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    startProgram();
                } else {
                    askForPermissions();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(final List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).onSameThread().check();
    }

    private void startProgram() {
        final Intent intent = new Intent(PermissionActivity.this, ListLocationsActivity.class);
        startActivity(intent);
        finish();
    }
}