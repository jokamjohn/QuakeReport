package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            //Show preference summary when the settings activity is launched.
            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            //Bind list preference and also set on click listener
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            //Bind limit preference
            Preference limit = findPreference(getString(R.string.setting_limit_key));
            bindPreferenceSummaryToValue(limit);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            //Listen for when the preference changes.
            preference.setOnPreferenceChangeListener(this);

            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext());

            //Get new preference value by key.
            String preferenceString = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            //Set the summary of the preference
            String value = newValue.toString();

            //Set correct label for list preference
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int indexPreference = listPreference.findIndexOfValue(value);//Find the index of the value
                if (indexPreference >= 0) {
                    CharSequence[] label = listPreference.getEntries();
                    preference.setSummary(label[indexPreference]);
                }
            } else {
                preference.setSummary(value);
            }
            return true;
        }
    }
}
