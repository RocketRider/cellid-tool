package de.rrsoftware.cellid_tool.ui.locations;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.RemoteViews;

import java.io.File;

import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.model.CellLocationManager;

public class CellWidget extends AppWidgetProvider {
    public static void sendUpdate(Context context) {
        Intent intent = new Intent(context, CellWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, CellWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CellLocationManager lc = CellLocationManager.getInstance(context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cell_widget);
        String desc = "Unbekannt";
        int cellId = 0;
        if (tm != null) {
            GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
            if (cellLocation != null) {
                cellId = cellLocation.getCid();
                if (lc.isCellKnown(cellId)) {
                    desc = lc.getDescription(cellId);
                }
            }
        }

        File imageFile = new File(context.getFilesDir(), cellId + ".jpg");
        if (imageFile.exists()) {
            views.setImageViewBitmap(R.id.widgetIcon, BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        } else {
            views.setImageViewResource(R.id.widgetIcon, R.drawable.ic_image_black_24dp);
        }

        views.setTextViewText(R.id.text, desc);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

