package com.example.android.googlebooklisting;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    public static final String LOG_TAG = BookListActivity.class.getName();

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     * */
    private static final int BOOK_LOADER_ID = 1;

    /** Adapter for the list of books */
    private BookAdapter mAdapter;

    // TODO: Create a edittext search
    private static final String USGS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=30";

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {

        Log.v(LOG_TAG,"onCreateLoader");
        return new BookLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        // TODO: Update the UI with the result
        //mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            updateUi(books);
        }
        Log.v(LOG_TAG,"onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        // TODO: Loader reset, so we can clear out our existing data.
        // Loader reset, so we can clear out our existing data.
        Log.v(LOG_TAG,"onLoaderReset");
        //mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        Log.v(LOG_TAG,"initLoader");
    }
    /**
     * Update the UI with the given book information.
     * @param books
     */

    private void updateUi(final ArrayList<Book> books) {

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of books
        BookAdapter mAdapter = new BookAdapter(this, books);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);
        /**
         * {@link AsyncTask} to perform the network request on a background thread, and then
         * * update the UI with the first book in the response.
         */

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the {@link Book} object at the given position the user clicked on
                Book book = books.get(position);
                String url = book.getUrl();
                // If the url is empty pops up a toast "no url provided"
                if (url.isEmpty()) {
                    Toast.makeText(view.getContext(), "No url provided", Toast.LENGTH_SHORT).show();
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });
    }
}
