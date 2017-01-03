package de.rrsoftware.cellid_tool.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class LocationService extends Service {
    private TelephonyManager tm;
    private CellListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        // don't provide binding, so return null
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        listener = new CellListener(getApplicationContext(), tm);
        tm.listen(listener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_CELL_INFO);
    }

    @Override
    public void onDestroy() {
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }
}
