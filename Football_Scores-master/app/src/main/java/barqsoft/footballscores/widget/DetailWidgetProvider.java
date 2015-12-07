package barqsoft.footballscores.widget;

/**
 * Created by perez.juan.jose on 29/11/2015.
 */
//public class DetailWidgetProvider {
//}
import android.annotation.TargetApi;
        import android.app.PendingIntent;
        import android.appwidget.AppWidgetManager;
        import android.appwidget.AppWidgetProvider;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.myFetchService;

//        import com.example.android.sunshine.app.DetailActivity;
//        import com.example.android.sunshine.app.MainActivity;
//        import com.example.android.sunshine.app.R;
//        import com.example.android.sunshine.app.sync.SunshineSyncAdapter;

/**
 * Provider for a scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetProvider extends AppWidgetProvider {

    protected final String LOG_TAG = getClass().getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {



        //Call the server to get new every 45 min
        Intent service_start = new Intent(context, myFetchService.class);
        context.startService(service_start);



        Log.i(LOG_TAG, "On Update recived");
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_detail);

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Set up the collection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
                Log.i(LOG_TAG, "Set the remote Id Adapter");
            } else {
                setRemoteAdapterV11(context, views);
            }
//            boolean useDetailActivity = context.getResources()
//                    .getBoolean(R.bool.use_detail_activity);
            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
//                    ? new Intent(context, DetailActivity.class)
//                    : new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

            views.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);

        Log.i(LOG_TAG, "LLego Mensaje: " + intent.getAction());

        if (myFetchService.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }

//        if (SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
//                    new ComponentName(context, getClass()));
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
//        }
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, DetailWidgetRemoteViewsService.class));
        Log.i(LOG_TAG, "Set remoteAdapte ICE cream");
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context, DetailWidgetRemoteViewsService.class));
        Log.i(LOG_TAG, "Set remoteAdapterV11");
    }
}