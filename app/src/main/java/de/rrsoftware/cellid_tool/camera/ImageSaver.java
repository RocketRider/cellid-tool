package de.rrsoftware.cellid_tool.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Saves a JPEG Image into the specified File.
 */
class ImageSaver implements Runnable {
    private static final float MAX_SIZE = 128;

    private final Image mImage;
    private final File mFile;

    ImageSaver(Image image, File file) {
        mImage = image;
        mFile = file;
    }


    private Bitmap getBitmap(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void run() {
        int oWidth = mImage.getWidth();
        int oHeight = mImage.getHeight();
        float scale;

        if (oWidth > oHeight) {
            scale = MAX_SIZE / oWidth;
        } else {
            scale = MAX_SIZE / oHeight;
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(getBitmap(mImage), (int) (oWidth * scale), (int) (oHeight * scale), true);

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(mFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 95, output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mImage.close();
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}