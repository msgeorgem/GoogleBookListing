package com.example.android.googlebooklisting;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {
    public static final String LOG_TAG = BookListActivity.class.getName();
    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    private static final String GBOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?";
    private static String QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY";
    public ListView bookListView;
    public BookAdapter mAdapter;
    public int index;
    Parcelable state;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle args) {

        Log.v(LOG_TAG, "onCreateLoader");
        //String query = getString(R.string.settings_query_default);
        String query = "android";
        if (args != null) {
            Log.i(LOG_TAG, "argument is not null");
            // Extract the search query from the arguments.
            query = args.getString(QUERY_EXTRA_KEY);
        }

        Uri baseUri = Uri.parse(GBOOKS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", query);
        uriBuilder.appendQueryParameter("maxResults", "30");

        return new BookLoader(this, uriBuilder.toString());


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        // Hide loading indicator because the data has been loaded
        Log.i(LOG_TAG, "onLoadFinished");
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No internet connection"
        mEmptyStateTextView.setText(R.string.no_books);
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
        Log.v(LOG_TAG, "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.v(LOG_TAG, "onLoaderReset");
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        
        // Find a reference to the {@link ListView} in the layout
        bookListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of books
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);
        
        //Initiate the Book Loader
        getLoaderManager().initLoader(0, null, this);
        
        // Get the launch Intent        
        handleIntent(getIntent());
        


        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the {@link Book} object at the given position the user clicked on
                Book book = mAdapter.getItem(position);
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
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);


        // Get details on the currently active default data network
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this).forceLoad();
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Set empty state text to display "No internet connection"
            mEmptyStateTextView.setText(R.string.no_books);
        }
    }

    @Override
    public void onPause() {
        // Save ListView state @ onPause
        Log.d(LOG_TAG, "saving listview state @ onPause");
        state = bookListView.onSaveInstanceState();
        index = bookListView.getFirstVisiblePosition();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getAdapter() != null) {
            bookListView.setAdapter(mAdapter);
            bookListView.setSelectionFromTop(index, 0);
            if (state != null) {
                bookListView.requestFocus();
                bookListView.onRestoreInstanceState(state);
            }
        }
    }
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        // If the Activity was started to service a Search request,
        // extract the search query.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);

            // Perform the search, passing in the search query as an argument
            // to the Cursor Loader
            Bundle args = new Bundle();
            args.putString(QUERY_EXTRA_KEY, searchQuery);

            // Restart the Cursor Loader to execute the new query.
            getLoaderManager().restartLoader(0, args, this);
        }
    }

}
