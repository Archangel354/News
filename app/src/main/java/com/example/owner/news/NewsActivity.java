package com.example.owner.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<NewsList>> {

    /**
     * Constant value for the booklist loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOKLIST_LOADER_ID = 1;

    /** Adapter for the list of books */
    private NewsAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Removes the circular loading indicator when it is not needed
        removeLoadingIndicator();

        // Recieves the url from the MainActivity via the Intent
        Intent mIntent = getIntent();
        urlString = mIntent.getExtras().getString("urlString");
        Log.i("LOG.BOOKLISTACTIVITY","The url is: " + urlString);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of BookList as input
        mAdapter = new NewsAdapter(this, new ArrayList<NewsList>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOKLIST_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            bookListView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    // Removes the circular loading indicator when it is not needed
    public void removeLoadingIndicator(){
        // Remove the loading indicator
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public Loader<List<NewsList>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new NewsLoader(this, urlString);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsList>> loader, List<NewsList> newsRecords) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        if (newsRecords == null || newsRecords.isEmpty()) {
            // Set empty state text to display "No newsRecords found."
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

            mEmptyStateTextView.setText(R.string.no_news);
        }
        // Clear the adapter of previous booklist data
        mAdapter.clear();

        // If there is a valid list of newsRecords, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsRecords != null && !newsRecords.isEmpty()) {
            mAdapter.addAll(newsRecords);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsList>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}

