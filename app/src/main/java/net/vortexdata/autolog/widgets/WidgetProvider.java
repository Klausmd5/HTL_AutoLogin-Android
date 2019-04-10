package net.vortexdata.autolog.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import net.vortexdata.autolog.MainActivity;
import net.vortexdata.autolog.Qconn;
import net.vortexdata.autolog.QuickConn;
import net.vortexdata.autolog.R;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            //Create a pending intent for a widget click
                Intent intent = new Intent(context, Qconn.class);
            //TheMainActivity is the class to which the intent is needed to be sent

            //new QuickConn(context);
            PendingIntent pIntentNetworkInfo = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.qconn_widget);
            remoteViews.setOnClickPendingIntent(R.id.q_widget_btn, pIntentNetworkInfo);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);


        }
    }


}
