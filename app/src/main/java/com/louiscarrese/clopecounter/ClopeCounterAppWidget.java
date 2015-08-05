package com.louiscarrese.clopecounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.louiscarrese.clopecounter.business.JourBusiness;
import com.louiscarrese.clopecounter.model.Jour;
import com.louiscarrese.clopecounter.model.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Implementation of App Widget functionality.
 */
public class ClopeCounterAppWidget extends AppWidgetProvider {
    private static final String ADD_CLICKED    = "clopeCounterWidgetAddClopeClick";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ADD_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.clope_counter_app_widget);
            watchWidget = new ComponentName(context, ClopeCounterAppWidget.class);


            JourBusiness utils = new JourBusiness(context);

            Jour jour = utils.addClope();

            updateCounter(remoteViews, jour);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        //Handle the Realm configuration
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .name("default.realm")
                .schemaVersion(1) //TODO: Mettre ça quelque part en conf peut être ?
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        try {
            Realm realm = Realm.getInstance(realmConfig);
            realm.close();
        } catch(Exception e) {
            Log.e("MainActivity", "Error getting realm", e);
            return;
        }

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    protected void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        JourBusiness utils = new JourBusiness(context);

        Jour jour = utils.getToday();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clope_counter_app_widget);

        //Update the number shown
        updateCounter(views, jour);

        //Make the button clickable
        views.setOnClickPendingIntent(R.id.appwidget_addclope, getPendingSelfIntent(context, ADD_CLICKED));


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    protected void updateCounter(RemoteViews views, Jour jour) {
        views.setTextViewText(R.id.appwidget_nbclopes, String.format("%d", jour.getNbClopes()));
        views.setTextViewText(R.id.appwidget_avg7, String.format("%.2g", jour.getAvg7()));

        if(jour.getNbClopes() > jour.getAvg7()) {
            views.setTextColor(R.id.appwidget_nbclopes, Color.RED);
        }

    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

