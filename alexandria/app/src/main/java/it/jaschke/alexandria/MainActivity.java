package it.jaschke.alexandria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import it.jaschke.alexandria.api.Callback;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, Callback {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence title;
    public static boolean IS_TABLET = false;
    public static boolean RIGTH_CONTAINER_DETAIL = false;
    public static boolean GO_BACK_PRESED = false;
    private BroadcastReceiver messageReciever;
    private String mEan=null;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";
    protected final String TAG = "jj"+getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IS_TABLET = isTablet();
        if(IS_TABLET){
            setContentView(R.layout.activity_main_tablet);
            Log.i(TAG, "on Create Tablet");
//            Log.i(TAG, "Rigth_container Detalle" +RIGTH_CONTAINER_DETAIL);
//            if(findViewById(R.id.right_container) != null & mEan!=null) {
//                Log.i(TAG, "onItemSelected: Right_Container con ean");
//                onItemSelected(mEan);
//            }
//
//            if(RIGTH_CONTAINER_DETAIL){
//                getSupportFragmentManager().popBackStack();
//            }
        }else {
            setContentView(R.layout.activity_main);
            Log.i(TAG, "on Create regular");
        }

        messageReciever = new MessageReciever();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReciever,filter);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment nextFragment;
        Log.i(TAG, "onNavigationDrawer");
        switch (position){
            default:
            case 0:
                nextFragment = new ListOfBooks();
                Log.i(TAG, "onNavigationDrawer: ListOffBooks");
                break;

            case 1:
                nextFragment = new AddBook();
                Log.i(TAG, "onNavigationDrawer: addBook");
                break;
            case 2:
                nextFragment = new About();
                Log.i(TAG, "onNavigationDrawer: About");
                break;

        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, nextFragment)
                //.addToBackStack(null)
                .addToBackStack((String) title)
                .commit();
        Log.i(TAG, "onNavigationDrawer: Commit");
    }

    public void setTitle(int titleId) {
        title = getString(titleId);
    }

    public void restoreActionBar() {
        Log.i(TAG, "RestoreActionBar");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);


//            int id = R.id.right_container;
//            // BookDetail details = (BookDetail) getSupportFragmentManager().findFragmentById(R.id.right_container);
//            Fragment   f = getSupportFragmentManager().findFragmentById(id);
//            getSupportFragmentManager().beginTransaction().remove(f).commit();
////

//            RIGTH_CONTAINER_DETAIL=true;
//            GO_BACK_PRESED=true;//when we add a book, the detaill book must go to onPause, like the gobackpresed!!!
//        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i(TAG, "Option Item Selected");
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReciever);
        super.onDestroy();
    }

    @Override
    public void onItemSelected(String ean) {
        mEan= ean;
        Log.i(TAG, "onItemSelected");
        Bundle args = new Bundle();
        args.putString(BookDetail.EAN_KEY, ean);

        BookDetail fragment = new BookDetail();
        fragment.setArguments(args);

        // Check what fragment is currently shown, replace if needed.
//        BookDetail details = (BookDetail) getSupportFragmentManager().findFragmentById(R.id.right_container);
//        details.get
        int id;
        if(findViewById(R.id.right_container) != null){
            Log.i(TAG, "onItemSelected: Right_Container");
            id = R.id.right_container;
           // BookDetail details = (BookDetail) getSupportFragmentManager().findFragmentById(R.id.right_container);
            getSupportFragmentManager().beginTransaction()
                    .replace(id, fragment)
//                    .replace(R.id.container,new ListOfBooks())
                            //.addToBackStack(null)
                    .addToBackStack("Book Detail")
                    .commit();
            RIGTH_CONTAINER_DETAIL=true;
            GO_BACK_PRESED=true;//when we add a book, the detaill book must go to onPause, like the gobackpresed!!!
        }
        else {
            id = R.id.container;
            Log.i(TAG, "onItemSelected: Normal Container");
            getSupportFragmentManager().beginTransaction()
                    .replace(id, fragment)
                    //.addToBackStack(null)
                    .addToBackStack("Book Detail")
                    .commit();
            RIGTH_CONTAINER_DETAIL=false;
        }


    }

    private class MessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra(MESSAGE_KEY)!=null){
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goBack(View view){


        GO_BACK_PRESED=true;
        getSupportFragmentManager().popBackStack();
        Log.i(TAG, "onGoBack-popBackStack right  true");
    }




    private boolean isTablet() {
        Log.i(TAG, "is tablet?");
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        if(getSupportFragmentManager().getBackStackEntryCount()<2){
            finish();
        }
        GO_BACK_PRESED=true;
        super.onBackPressed();
    }


}