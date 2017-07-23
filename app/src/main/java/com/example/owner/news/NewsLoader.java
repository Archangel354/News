package com.example.owner.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Owner on 7/23/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsList>> {

    /** Query URL */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsList> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<NewsList> newsRecords = Utils.fetchNewsData(mUrl);
        return newsRecords;
    }


}
