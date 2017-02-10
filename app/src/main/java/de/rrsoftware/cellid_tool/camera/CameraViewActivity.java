package de.rrsoftware.cellid_tool.camera;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;

import com.google.android.cameraview.CameraView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.rrsoftware.cellid_tool.R;

public class CameraViewActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String LOGTAG = "CamActivity";
    public static final String CELL_ID = "CellId";

    @BindView(R.id.camera)
    CameraView cameraView;
    private Handler backgroundHandler;
    private int cellId;

    private CameraView.Callback cameraCallback = new CameraView.Callback() {
        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(LOGTAG, "take picture: " + cellId);
            getBackgroundHandler().post(new ImageSaver(data, new File(getFilesDir(), cellId + ".jpg"), getRotation()));
            finish();
        }
    };

    @OnClick(R.id.take_picture)
    void takePicture() {
        if (cameraView != null && cameraView.isCameraOpened()) {
            cameraView.takePicture();
        }
    }

    private int getRotation() {
        final int orientation = getResources().getConfiguration().orientation;
        int rotation = 0;
        if (Surface.ROTATION_0 == orientation) {
            rotation = 0;
        } else if (Surface.ROTATION_180 == orientation) {
            rotation = 180;
        } else if (Surface.ROTATION_90 == orientation) {
            rotation = 90;
        } else if (Surface.ROTATION_270 == orientation) {
            rotation = 270;
        }
        return rotation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        ButterKnife.bind(this);
        cameraView.addCallback(cameraCallback);
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
            backgroundHandler.getLooper().quitSafely();
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