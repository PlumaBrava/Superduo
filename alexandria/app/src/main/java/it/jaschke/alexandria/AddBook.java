package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.zxing.client.android.camera.CameraConfigurationUtils.*;
//import com.google.zxing.client.android.SCAN

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.services.DownloadImage;


public class AddBook extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    //Boprivate static final String TAG = "INTENT_TO_SCAN_ACTIVITY";
    private EditText ean;
    private final int LOADER_ID = 1;
    private View rootView;
    private final String EAN_CONTENT="eanContent";
    private static final String SCAN_FORMAT = "scanFormat";
    private static final String SCAN_CONTENTS = "scanContents";

    private String mScanFormat = "Format:";
    private String mScanContents = "Contents:";

    protected final String TAG = getClass().getSimpleName();

    Fragment myFragment = this;
    public AddBook(){
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(ean!=null) {
            outState.putString(EAN_CONTENT, ean.getText().toString());
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        ean = (EditText) rootView.findViewById(R.id.ean);

        ean.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //no need
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //no need
            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i(TAG, "Codigo leido " +s.toString());
            // Veroficar si hay conexion a internet

                if (!utility.isNetworkAvailable(getActivity())) {

                    Toast toast = Toast.makeText(getActivity(), getActivity().getText(R.string.No_internet_onection), Toast.LENGTH_SHORT);
                    toast.show();

                    Log.i(TAG, "sin REd ");
                }

                else {

                    String ean = s.toString();
                    //catch isbn10 numbers
                    if (ean.length() == 10 && !ean.startsWith("978")) {
                        ean = "978" + ean;
                    }
                    if (ean.length() < 13) {
                        clearFields();
                        return;
                    }

                    //  Extra error case
                    //  The ean Cod has 13 digits an its the only number we can found.

                    if (ean.length() == 13) {
                        Log.i(TAG, "ean.length "+ean.length());
                        //Once we have an ISBN, start a book intent
                    Intent bookIntent = new Intent(getActivity(), BookService.class);
                    bookIntent.putExtra(BookService.EAN, ean);
                    bookIntent.setAction(BookService.FETCH_BOOK);
                    getActivity().startService(bookIntent);
                    AddBook.this.restartLoader();

                    }

                    else{
                        Toast toast = Toast.makeText(getActivity(), getActivity().getText(R.string.Bar_Cod_is_not_EAN13), Toast.LENGTH_SHORT);
                        toast.show();

                        Log.i(TAG, "Bar Cod is not EAN13! ");
                    }
                }
            }
        });

        rootView.findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is the callback method that the system will invoke when your button is
                // clicked. You might do this by launching another app or by including the
                //functionality directly in this app.
                // Hint: Use a Try/Catch block to handle the Intent dispatch gracefully, if you
                // are using an external app.
                //when you're done, remove the toast below.
                ean.setText("");
                if (!utility.isNetworkAvailable(getActivity())) {

                    Toast toast = Toast.makeText(getActivity(), getActivity().getText(R.string.No_internet_onection), Toast.LENGTH_SHORT);
                    toast.show();

                    Log.i(TAG, "sin REd ");

                }

                else {
                    Context context = getActivity();

                    int duration = Toast.LENGTH_SHORT;
                    Log.i(TAG, "Llamado IntentIntegrator a ");
                    IntentIntegrator integrator = new IntentIntegrator(getActivity());
                    Log.i(TAG, "initiateScan b ");
                    //  Library Referencies https://github.com/journeyapps/zxing-android-embedded

                    integrator.forSupportFragment(myFragment).initiateScan();


                }
            }
        });

        rootView.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ean.setText("");
                //clear the view
                clearFields();
            }
        });

        rootView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(getActivity(), BookService.class);
                bookIntent.putExtra(BookService.EAN, ean.getText().toString());
                bookIntent.setAction(BookService.DELETE_BOOK);
                getActivity().startService(bookIntent);
                ean.setText("");

                //clear the view
                clearFields();
            }
        });

        if(savedInstanceState!=null){
            ean.setText(savedInstanceState.getString(EAN_CONTENT));
            ean.setHint("");
        }

        return rootView;
    }

    private void restartLoader(){
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(ean.getText().length()==0){
            return null;
        }
        String eanStr= ean.getText().toString();
        if(eanStr.length()==10 && !eanStr.startsWith("978")){
            eanStr="978"+eanStr;
        }
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(eanStr)),
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

        String bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        ((TextView) rootView.findViewById(R.id.bookTitle)).setText(bookTitle);

        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        ((TextView) rootView.findViewById(R.id.bookSubTitle)).setText(bookSubTitle);

        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        Log.i(TAG, "AUTOI: " + authors);
        String[] authorsArr ;
        if (authors != null) {
            authorsArr = authors.split(",");
            ((TextView) rootView.findViewById(R.id.authors)).setLines(authorsArr.length);
            ((TextView) rootView.findViewById(R.id.authors)).setText(authors.replace(",","\n"));
        }
        else{
            ((TextView) rootView.findViewById(R.id.authors)).setText(getActivity().getText(R.string.unknowAutors));
        }


        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        if(Patterns.WEB_URL.matcher(imgUrl).matches()){
            //usar Picaso
            //new DownloadImage((ImageView) rootView.findViewById(R.id.bookCover)).execute(imgUrl);

            Picasso.with(getActivity())
                    .load(imgUrl)
                    .into((ImageView) rootView.findViewById(R.id.bookCover));
            rootView.findViewById(R.id.bookCover).setVisibility(View.VISIBLE);
        }

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        ((TextView) rootView.findViewById(R.id.categories)).setText(categories);

        rootView.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    private void clearFields(){
        ((TextView) rootView.findViewById(R.id.bookTitle)).setText("");
        ((TextView) rootView.findViewById(R.id.bookSubTitle)).setText("");
        ((TextView) rootView.findViewById(R.id.authors)).setText("");
        ((TextView) rootView.findViewById(R.id.categories)).setText("");
        rootView.findViewById(R.id.bookCover).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.delete_button).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.scan);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "Llamado onActivityResult " );
        Log.i(TAG, "requestCode" +requestCode  );
        Log.i(TAG, "resultCode " +resultCode );
        Log.i(TAG, "intent"+  intent);

//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                String contents = intent.getStringExtra("SCAN_RESULT");
//                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
//                // Handle successful scan
//            } else if (resultCode == RESULT_CANCELED) {
//                // Handle cancel
//            }


        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        // Extra Error Verification. If return without read a barcode (Content==null) we don't have information to fetch.
        if (scanningResult != null && scanningResult.getContents()!=null) {



            String scanContent = scanningResult.getContents();
            Log.i(TAG, "scanContent " +scanContent );
            String scanFormat = scanningResult.getFormatName();

            // Extra Error Verification. Code Type. If this code is not EAN_13 the fetch fail.
            if (scanFormat.equals("EAN_13")){
            Log.i(TAG, "scanFormat " + scanFormat);
            Toast toast = Toast.makeText(getActivity(), getActivity().getText(R.string.Received)+scanContent , Toast.LENGTH_SHORT);
            toast.show();
            ean.setText(scanContent);}
            else{
                Toast toast = Toast.makeText(getActivity(), getActivity().getText(R.string.Code_Type_Incorrect), Toast.LENGTH_SHORT);
                toast.show();}


        }else{
            Toast toast = Toast.makeText(getActivity(), getActivity().getText(R.string.No_scan_data_received), Toast.LENGTH_SHORT);

            toast.show();
        }
//we ha
//retrieve scan result
    }
}
