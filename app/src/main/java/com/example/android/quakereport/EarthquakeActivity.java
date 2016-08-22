/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_INT = 0;
    private EarthquakeAdapter mAdapter;
    private TextView mEmptyTv;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //Empty text view
        mEmptyTv = (TextView) findViewById(R.id.empty_text_view);
        earthquakeListView.setEmptyView(mEmptyTv);

        //Progress bar
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        if (isConnected()) {
            //Fetch data from the network.
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_INT, null, this);
            Log.i(LOG_TAG, "Loader init loader");
        }else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyTv.setText(R.string.no_internet);
        }

        // Create a new {@link ArrayAdapter} of empty earthquakes list
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Loader created");
        //Get preference String.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = preferences
                .getString(getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = preferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String limit = preferences.getString(getString(R.string.setting_limit_key),
                getString(R.string.setting_limit_default));

        Uri baseUri = Uri.parse(QueryUtils.USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("eventtype", "earthquake");
        uriBuilder.appendQueryParameter("orderby",orderBy);
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("limit", limit);

        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        mAdapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);
        }

        //Set text for empty list text view
        mEmptyTv.setText(R.string.no_earhtquake_data);

        //Hide progress bar
        mProgressBar.setVisibility(View.GONE);

        Log.i(LOG_TAG, "Loader finished loading");
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        //Clear the adapter
        mAdapter.clear();
        Log.i(LOG_TAG, "Loader reset");
    }

    /**Check network and internet connectivity.
     *
     * @return boolean
     */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
