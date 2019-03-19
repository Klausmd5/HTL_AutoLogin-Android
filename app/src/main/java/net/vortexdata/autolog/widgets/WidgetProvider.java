package net.vortexdata.autolog.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.vortexdata.autolog.QuickConn;
import net.vortexdata.autolog.R;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent i = new Intent(context, QuickConn.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i,0);

            RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.qconn_widget);
            v.setOnClickPendingIntent(R.id.q_widget_btn, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, v);
        }
    }

}
