package de.rrsoftware.cellid_tool.ui.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Saves a JPEG Image into the specified File.
 */
class ImageSaver implements Runnable {
    private static String LOGTAG = "ImageServer";
    private static final float MAX_SIZE = 128;
    private final byte[] image;
    private final File file;
    private final float angle;

    ImageSaver(byte[] image, File file, float angle) {
        this.image = image;
        this.file = file;
        this.angle = angle;
    }


    @Override
    public void run() {
        Bitmap image = BitmapFactory.decodeByteArray(this.image, 0, this.image.length);
        int oWidth = image.getWidth();
        int oHeight = image.getHeight();
        float scale;

        if (oWidth > oHeight) {
            scale = MAX_SIZE / oWidth;
        } else {
            scale = MAX_SIZE / oHeight;
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, (int) (oWidth * scale), (int) (oHeight * scale), true);

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);


        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 99, output);
        } catch (IOException e) {
            Log.e(LOGTAG, "save failed", e);
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    Log.e(LOGTAG, "close failed", e);
                }
            }
        }
    }
}