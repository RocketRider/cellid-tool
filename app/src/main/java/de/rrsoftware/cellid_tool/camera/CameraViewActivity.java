package de.rrsoftware.cellid_tool.camera;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.cameraview.CameraView;

import java.io.File;

import de.rrsoftware.cellid_tool.R;

public class CameraViewActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String LOGTAG = "CamActivity";
    public static final String CELL_ID = "CellId";

    private CameraView cameraView;
    private Handler backgroundHandler;
    private int cellId;

    private CameraView.Callback cameraCallback = new CameraView.Callback() {
        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(LOGTAG, "take picture: " + cellId);
            getBackgroundHandler().post(new ImageSaver(data, new File(getFilesDir(), cellId + ".jpg")));
            CameraViewActivity.this.cameraView.stop();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        cameraView = (CameraView) findViewById(R.id.camera);
        if (cameraView != null) {
            cameraView.addCallback(cameraCallback);
        }
        FloatingActionButton takePicture = (FloatingActionButton) findViewById(R.id.take_picture);
        if (takePicture != null) {
            takePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cameraView != null && cameraView.isCameraOpened()) {
                        cameraView.takePicture();
                    }
                }
            });
        }
        cellId = getIntent().getIntExtra(CELL_ID, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                backgroundHandler.getLooper().quitSafely();
            } else {
                backgroundHandler.getLooper().quit();
            }
            backgroundHandler = null;
        }
    }

    private Handler getBackgroundHandler() {
        if (backgroundHandler == null) {
            HandlerThread thread = new HandlerThread("savePic");
            thread.start();
            backgroundHandler = new Handler(thread.getLooper());
        }
        return backgroundHandler;
    }
}