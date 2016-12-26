package de.rrsoftware.cellid_tool;

import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import java.util.List;

public class CellListener extends PhoneStateListener {
    private TelephonyManager tm;

    public CellListener(TelephonyManager tm) {
        this.tm = tm;
    }

    @Override
    public void onCellInfoChanged(List<CellInfo> cellInfo) {
        checkCell();
    }

    @Override
    public void onCellLocationChanged(CellLocation location) {
        checkCell();
    }


    public void checkCell() {
        GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
        cellLocation.toString();
    }
}
