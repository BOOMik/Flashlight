package ru.boomik.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class FlashlightWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, boolean state) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flashlight_widget);
        views.setImageViewResource(R.id.image, state ? R.drawable.widget_enabled : R.drawable.widget_disabled);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(FlashlightReceiver.ACTION_SWITCH), 0);
        //регистрируем наше событие
        views.setOnClickPendingIntent(R.id.image, actionPendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        boolean state = FlashlightController.getInstance(context).isEnabled();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, state);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void updateAll(Context context, boolean state) {
        ComponentName thisWidget = new ComponentName( context, FlashlightWidget.class );
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(thisWidget);
        for (int appWidgetId : ids) {
            updateAppWidget(context, manager, appWidgetId, state);
        }

    }
}

