package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.services.DownloadImage;


public class BookDetail extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EAN_KEY = "EAN";
    private final int LOADER_ID = 10;
    private View rootView;
    private String ean;
    private String bookTitle;
    private ShareActionProvider shareActionProvider;
    protected final String TAG = getClass().getSimpleName();
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;


    public BookDetail(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate bookDetail");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView BookDetail");
        Bundle arguments = getArguments();
        if (arguments != null) {
            ean = arguments.getString(BookDetail.EAN_KEY);
            Log.i(TAG, "codigo ean:"+ ean);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        rootView = inflater.inflate(R.layout.fragment_full_book, container, false);
        rootView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(getActivity(), BookService.class);
                bookIntent.putExtra(BookService.EAN, ean);
                bookIntent.setAction(BookService.DELETE_BOOK);
                getActivity().startService(bookIntent);
                MainActivity.GO_BACK_PRESED=false;
                onPause();
                mCallbacks.onNavigationDrawerItemSelected(0);

                //getActivity().getSupportFragmentManager().popBackStack();
//               MainActivity.GO_BACK_PRESED=true;
//                onPause();
  //              mCallbacks.onNavigationDrawerItemSelected(0);
//                if (MainActivity.RIGTH_CONTAINER_DETAIL==false) {
//                    getActivity().getSupportFragmentManager().popBackStack();
//                } else {
//                    onPause();
//                }
            }
        });
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Extra error: If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if( bookTitle!=null) {
            Log.i(TAG, "bookTite: " + bookTitle);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + bookTitle);

            shareActionProvider.setShareIntent(shareIntent);

        }

    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(ean)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        ((TextView) rootView.findViewById(R.id.fullBookTitle)).setText(bookTitle);
//        Log.i(TAG, "bookTite: " + bookTitle);
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + bookTitle);
//
//                shareActionProvider.setShareIntent(shareIntent);

        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        ((TextView) rootView.findViewById(R.id.fullBookSubTitle)).setText(bookSubTitle);

        String desc = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.DESC));
        ((TextView) rootView.findViewById(R.id.fullBookDesc)).setText(desc);

        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));

//        String[] authorsArr = authors.split(",");

        String[] authorsArr ;
        if (authors != null) {
            authorsArr = authors.split(",");
            ((TextView) rootView.findViewById(R.id.authors)).setLines(authorsArr.length);
            ((TextView) rootView.findViewById(R.id.authors)).setText(authors.replace(",","\n"));
        }
        else{
            ((TextView) rootView.findViewById(R.id.authors)).setText(getActivity().getText(R.string.unknowAutors));
        }
//        ((TextView) rootView.findViewById(R.id.authors)).setLines(authorsArr.length);
//        ((TextView) rootView.findViewById(R.id.authors)).setText(authors.replace(",","\n"));
        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        if(Patterns.WEB_URL.matcher(imgUrl).matches()){

           // new DownloadImage((ImageView) rootView.findViewById(R.id.fullBookCover)).execute(imgUrl);

            Picasso.with(getActivity())
                    .load(imgUrl)
                    .into((ImageView) rootView.findViewById(R.id.fullBookCover));

            rootView.findViewById(R.id.fullBookCover).setVisibility(View.VISIBLE);
        }

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        ((TextView) rootView.findViewById(R.id.categories)).setText(categories);


        // Extra Error it has no sence R.id.right_container is always null
//        if(rootView.findViewById(R.id.right_container)!=null){
//            rootView.findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
//        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public void onPause() {

        // Extra Error: the original code doesn't work properly. The navegation is not correct. This modification works better
        super.onPause();
        Log.i(TAG, "onPause");

        if(MainActivity.IS_TABLET && MainActivity.RIGTH_CONTAINER_DETAIL==false && MainActivity.GO_BACK_PRESED==false ) {
           getActivity().getSupportFragmentManager().popBackStack();
            //super.onDestroy();
            Log.i(TAG, "onPause vertical horizontal");
        }else if(MainActivity.IS_TABLET && MainActivity.RIGTH_CONTAINER_DETAIL==true && MainActivity.GO_BACK_PRESED==false ){

            getActivity().getSupportFragmentManager().popBackStack();
//            mCallbacks.onNavigationDrawerItemSelected(0);
            Log.i(TAG, "onPause horizontal Vertical");
            super.onDestroy();

        }
        else{
            MainActivity.GO_BACK_PRESED=false;
            Log.i(TAG, "onPause right por go Back");
        }

//        Log.i(TAG, "onPause-destroyView");
       // super.onDestroyView();

//        if(MainActivity.IS_TABLET && rootView.findViewById(R.id.right_container)==null){
//
//            getActivity().getSupportFragmentManager().popBackStack();
//            Log.i(TAG, "onPause (tablet y right== null-popBackStack");
//        }
    }

    @Override
    public void onDestroy() {

        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }



}