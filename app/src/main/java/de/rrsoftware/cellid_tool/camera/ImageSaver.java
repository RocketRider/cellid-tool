package de.rrsoftware.cellid_tool.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private final byte[] mImage;
    private final File mFile;

    ImageSaver(byte[] image, File file) {
        mImage = image;
        mFile = file;
    }

    @Override
    public void run() {
        Bitmap image = BitmapFactory.decodeByteArray(mImage, 0, mImage.length);
        int oWidth = image.getWidth();
        int oHeight = image.getHeight();
        float scale;

        if (oWidth > oHeight) {
            scale = MAX_SIZE / oWidth;
        } else {
            scale = MAX_SIZE / oHeight;
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, (int) (oWidth * scale), (int) (oHeight * scale), true);

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(mFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 99, output);
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