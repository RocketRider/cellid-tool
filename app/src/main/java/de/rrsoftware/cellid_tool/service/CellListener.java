package de.rrsoftware.cellid_tool.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.util.List;

import de.rrsoftware.cellid_tool.ui.locations.CellWidget;
import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.model.CellLocationManager;
import de.rrsoftware.cellid_tool.ui.locations.RegisterLocationActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

class CellListener extends PhoneStateListener {
    private static final String LOGTAG = "CellListener";
    private final Context context;
    private final TelephonyManager tm;
    private final CellLocationManager lc;

    CellListener(final Context context, final TelephonyManager tm) {
        this.context = context;
        this.tm = tm;
        lc = CellLocationManager.getInstance(context);
    }

    @Override
    public void onCellInfoChanged(final List<CellInfo> cellInfo) {
        checkCell();
    }

    @Override
    public void onCellLocationChanged(final CellLocation location) {
        checkCell();
    }


    private void checkCell() {
        final GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
        if (cellLocation != null) {
            Log.d(LOGTAG, cellLocation.toString());
            if (!lc.isCellKnown(cellLocation.getCid())) {
                Log.d(LOGTAG, "Unknown cell: " + cellLocation.toString());
                notityUser(cellLocation.getCid());
            } else {
                Log.d(LOGTAG, "Already known cell: " + cellLocation.toString());
            }
        } else {
            Log.e(LOGTAG, "cellLocation is null");
        }

        CellWidget.sendUpdate(context);
    }

    private void notityUser(final int cellid) {
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_vpn_lock_black_24dp)
                        .setContentTitle("Neue Zelle gefunden")
                        .setAutoCancel(true)
                        .setContentText(String.valueOf(cellid));

        final Intent resultIntent = new Intent(context, RegisterLocationActivity.class);
        resultIntent.putExtra(RegisterLocationActivity.CELL_ID, cellid);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build());
    }

}
