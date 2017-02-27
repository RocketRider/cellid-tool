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

public class CellLocationManager {
    private static final String LOGTAG = "CellLocationManager";
    private static CellLocationManager instance;

    private final File file;
    private final File dir;
    private Map<Integer, LocationItem> locations = new HashMap<>();

    private CellLocationManager(final Context context) {
        dir = context.getFilesDir();
        file = new File(dir, "map");
        loadLocations();
    }

    public static CellLocationManager getInstance(final Context context) {
        if (instance == null) {
            instance = new CellLocationManager(context);
        }
        return instance;
    }

    public Integer[] getAllCellIds() {
        return locations.keySet().toArray(new Integer[locations.keySet().size()]);
    }


    public String getDescription(final int cellid) {
        if (locations.containsKey(cellid)) {
            return locations.get(cellid).getDesc();
        } else {
            return "";
        }
    }

    public double getLatitude(final int cellid) {
        if (locations.containsKey(cellid)) {
            return locations.get(cellid).getLatitude();
        } else {
            return 0.;
        }
    }

    public double getLongitude(final int cellid) {
        if (locations.containsKey(cellid)) {
            return locations.get(cellid).getLongitude();
        } else {
            return 0.;
        }
    }

    public boolean isCellKnown(final int cellid) {
        return locations.containsKey(cellid);
    }

    public void addLocation(final int cellid, final String desc, final double latitude, final double longitude) {
        locations.put(cellid, new LocationItem(desc, latitude, longitude));
        saveLocations();
    }


    private void loadLocations() {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(new FileInputStream(file));
            locations = (Map<Integer, LocationItem>) stream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
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

    public void deleteLocation(final int cellId) {
        locations.remove(cellId);
        if (new File(dir, cellId + ".jpg").delete()) {
            Log.e(LOGTAG, "failed to delete file: " + cellId);
        }
        saveLocations();
    }
}
