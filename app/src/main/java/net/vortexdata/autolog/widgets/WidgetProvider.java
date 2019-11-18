package net.vortexdata.autolog.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.vortexdata.autolog.Qconn;
import net.vortexdata.autolog.R;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {


            Intent intent = new Intent(context, Qconn.class);

            PendingIntent pIntentNetworkInfo = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.qconn_widget);
            remoteViews.setOnClickPendingIntent(R.id.q_widget_btn, pIntentNetworkInfo);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);


        }
    }


}
