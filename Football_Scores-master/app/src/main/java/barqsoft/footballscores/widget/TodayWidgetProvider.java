package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.myFetchService;

/**
 * Created by perez.juan.jose on 27/11/2015.
 */
public class TodayWidgetProvider extends AppWidgetProvider {

    protected final String TAG = getClass().getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int weatherArtResourceId = R.drawable.manchester_city;

        Log.i(TAG, "On Update recived");
        context.startService(new Intent(context, TodayWidgetIntentService.class));

//        String description = "Clear";
//        double maxTemp = 24;
//        //String formattedMaxTemperature = Utility.formatTemperature(context, maxTemp);
//        String formattedMaxTemperature = "24";
//        // Perform this loop procedure for each Today widget
//        for (int appWidgetId : appWidgetIds) {
////            int layoutId = R.layout.widget_today_small;
//            int layoutId = R.layout.widget_today_large;
//            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
//
//            // Add the data to the RemoteViews
//            views.setImageViewResource(R.id.widget_icon, weatherArtResourceId);
//            // Content Descriptions for RemoteViews were only added in ICS MR1
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                setRemoteContentDescription(views, description);
//
//
//            }
//            views.setTextViewText(R.id.widget_home, "1Local");
//            views.setTextViewText(R.id.widget_away, "1Visitante");
//            views.setTextViewText(R.id.widget_result, "13 -2");
//
//            // Create an Intent to launch MainActivity
//            Intent launchIntent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
//            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//
//            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);

        }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget_icon, description);
    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        Log.i(TAG, "LLego Options Changed: " + newOptions.toString());
        context.startService(new Intent(context, TodayWidgetIntentService.class));

    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "LLego Mensaje: "+intent.getAction());


                if (myFetchService.ACTION_DATA_UPDATED.equals(intent.getAction())) {
                context.startService(new Intent(context,TodayWidgetIntentService.class));
                }


//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
//                new ComponentName(context, getClass()));
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);


//        if (SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
//                    new ComponentName(context, getClass()));
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
//        }
    }
}