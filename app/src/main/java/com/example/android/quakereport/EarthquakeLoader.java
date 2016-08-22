package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getSimpleName();
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();//This triggers the loadInBackground method
        Log.i(LOG_TAG, "Loader on start loading");
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG, "Loader load in background");

        if (mUrl == null){
            return null;
        }

        List<Earthquake> earthquakes = QueryUtils.makeHttpRequest(mUrl);

        return earthquakes;
    }

}
