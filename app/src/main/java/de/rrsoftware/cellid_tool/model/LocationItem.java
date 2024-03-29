package de.rrsoftware.cellid_tool.model;

import java.io.IOException;
import java.io.Serializable;

class LocationItem implements Serializable {
    private String desc;
    private double latitude;
    private double longitude;

    LocationItem(final String desc, final double latitude, final double longitude) {
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    String getDesc() {
        return desc;
    }

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }

    private void writeObject(final java.io.ObjectOutputStream stream)
            throws IOException {
        stream.writeObject(desc);
        stream.writeDouble(latitude);
        stream.writeDouble(longitude);
    }

    private void readObject(final java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        desc = (String) stream.readObject();
        latitude = stream.readDouble();
        longitude = stream.readDouble();
    }
}
