package de.rrsoftware.cellid_tool.model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {
    private static String LOGTAG = "LocationManager";
    private static LocationManager instance;

    private File file;
    private Map<Integer, String> locations = new HashMap<>();


    private LocationManager(Context context) {
        file = new File(context.getFilesDir(), "map");
        loadLocations();
    }

    public static LocationManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocationManager(context);
        }
        return instance;
    }


    public boolean isCellKnown(final int cellid) {
        return locations.containsKey(cellid);
    }

    public void addLocation(int cellid, String desc) {
        locations.put(cellid, desc);
        saveLocations();
    }


    private void loadLocations() {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(new FileInputStream(file));
            locations = (Map<Integer, String>) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.e(LOGTAG, "failed to open stream", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(LOGTAG, "failed to close stream", e);
                }
            } else {
                Log.e(LOGTAG, "Open Stream failed");
            }
        }
    }

    private void saveLocations() {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(locations);
            outputStream.flush();
        } catch (IOException e) {
            Log.e(LOGTAG, "failed to open stream", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(LOGTAG, "failed to close stream", e);
                }
            } else {
                Log.e(LOGTAG, "Open Stream failed");
            }
        }
    }

}
