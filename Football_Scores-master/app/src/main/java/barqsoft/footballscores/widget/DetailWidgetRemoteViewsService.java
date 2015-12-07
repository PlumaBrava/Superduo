package barqsoft.footballscores.widget;

/**
 * Created by perez.juan.jose on 29/11/2015.
 */
//public class DetailWidgetRemoteViewsService {
//}
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.Target;
//import com.example.android.sunshine.app.R;
//import com.example.android.sunshine.app.Utility;
//import com.example.android.sunshine.app.data.WeatherContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * RemoteViewsService controlling the data being shown in the scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();
    private static final String[] SCORE_COLUMNS = {
//            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
//            WeatherContract.WeatherEntry.COLUMN_DATE,
//            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
//            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
//            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
//            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
            DatabaseContract.SCORES_TABLE + "." + DatabaseContract.scores_table._ID,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_DAY,

    };
    // these indices must match the projection
    private static final int INDEX_SOCORE_ID = 0;
    private static final int INDEX_SCORE_HOME = 1;
    private static final int INDEX_SCORE_AWAY = 2;
    private static final int INDEX_SCORE_HOME_GOALS = 3;
    private static final int INDEX_SCORE_AWAY_GOALS = 4;
    private static final int INDEX_SCORE_MATCH_DAY = 5;
    private Cursor data = null;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            //private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();


                Uri scoresUri = DatabaseContract.scores_table.buildScoreWithDate();

                String[] date = new String[1];

                Date fragmentdate = new Date(System.currentTimeMillis());
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                date[0]=mformat.format(fragmentdate);

                data = getContentResolver().query(scoresUri,SCORE_COLUMNS,null, date,null);


                Log.i(LOG_TAG, "resultado Querry count: " +  data.getCount());
                Log.i(LOG_TAG, "resultado Querry column name: " +  data.getColumnName(1)+"-"+data.getColumnName(2)
                        +"-"+data.getColumnName(3)
                        +"-"+data.getColumnName(4)
                        +"-"+data.getColumnName(0));

//                WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
//                        locationSetting,
//                        dateInMillis);

//                String location = Utility.getPreferredLocation(DetailWidgetRemoteViewsService.this);
//                Uri weatherForLocationUri = WeatherContract.WeatherEntry
//                        .buildWeatherLocationWithStartDate(location, System.currentTimeMillis());
//                data = getContentResolver().query(weatherForLocationUri,
//                        FORECAST_COLUMNS,
//                        null,
//                        null,
//                        WeatherContract.WeatherEntry.COLUMN_DATE + " ASC");
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                Log.i(LOG_TAG, "on Destroy: " +  data.getCount());
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                if(data==null){

                    Log.i(LOG_TAG, "get count: Null");
                }
                else {
                    Log.i(LOG_TAG, "get count: "+data.getCount());
                }
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    Log.i(LOG_TAG, "Remote view Incorrect : " + position);
                    return null;

                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);
//                int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
//                int weatherArtResourceId = Utility.getIconResourceForWeatherCondition(weatherId);
//                Bitmap weatherArtImage = null;
//                if ( !Utility.usingLocalGraphics(DetailWidgetRemoteViewsService.this) ) {
//                    String weatherArtResourceUrl = Utility.getArtUrlForWeatherCondition(
//                            DetailWidgetRemoteViewsService.this, weatherId);
//                    try {
//                        weatherArtImage = Glide.with(DetailWidgetRemoteViewsService.this)
//                                .load(weatherArtResourceUrl)
//                                .asBitmap()
//                                .error(weatherArtResourceId)
//                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
//                    } catch (InterruptedException | ExecutionException e) {
//                        Log.e(LOG_TAG, "Error retrieving large icon from " + weatherArtResourceUrl, e);
//                    }
//                }/
//                views.setTextViewText(R.id.widget_home, "home");
//                views.setTextViewText(R.id.widget_result, "1-1");
//                views.setTextViewText(R.id.widget_away,  "away");

                views.setTextViewText(R.id.widget_home, data.getString(INDEX_SCORE_HOME));
                views.setTextViewText(R.id.widget_result, Utilies.getScores(data.getInt(INDEX_SCORE_HOME_GOALS), data.getInt(INDEX_SCORE_AWAY_GOALS)));
                views.setTextViewText(R.id.widget_away,  data.getString(INDEX_SCORE_AWAY));

                Log.i(LOG_TAG, "geting view: " + data.getInt(0));
                final Intent fillInIntent = new Intent();
//                String locationSetting =
//                        Utility.getPreferredLocation(DetailWidgetRemoteViewsService.this);
//                Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
//                        locationSetting,
//                        dateInMillis);
                Uri ScoreUri = DatabaseContract.scores_table.buildScoreWithDate();
                fillInIntent.setData(ScoreUri);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_icon, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                Log.i(LOG_TAG, "LoadingView: " );
                return new RemoteViews(getPackageName(), R.layout.widget_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                Log.i(LOG_TAG, "ViewTypeCount: " );
                return 1;
            }

            @Override
            public long getItemId(int position)
            {
                Log.i(LOG_TAG, "Get Item ID: " +  position);
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_SOCORE_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {

                Log.i(LOG_TAG, "has StableIds: " );
                return true;
            }
        };
    }
}