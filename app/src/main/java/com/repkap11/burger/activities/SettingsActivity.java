package com.repkap11.burger.activities;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.repkap11.burger.BuildConfig;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    public static final String PREF_NOTIFICATIONS_ENABLED = "pref_notifications_enabled";
    public static final String PREF_NOTIFICATIONS_RINGTONE = "pref_notifications_ringtone";
    public static final String PREF_NOTIFICATIONS_VIBRATE = "pref_notifications_vibrate";
    public static final String PREF_NOTIFICATIONS_LED = "pref_notifications_led";

    public static final String PREF_APP_VERSION = "pref_app_version";
    public static final String PREF_APP_BUILD_FLAVOR = "pref_app_build_flavor";


    private static final String TAG = SettingsActivity.class.getSimpleName();
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private Preference.OnPreferenceChangeListener mBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {


            String stringValue = value.toString();
            Log.e(TAG, "Looking at pref:" + preference.getKey());
            if (preference.getKey().equals(PREF_APP_VERSION)) {
                preference.setSummary(BurgerApplication.getAppVersionName(getApplicationContext()));
            } else if (preference.getKey().equals(PREF_APP_BUILD_FLAVOR)) {
                preference.setSummary(BuildConfig.FLAVOR);
            } else if (preference instanceof ListPreference) {
                Log.e(TAG, "Updated List:" + preference.getKey() + " to:" + stringValue);
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            } else if (preference instanceof RingtonePreference) {
                Log.e(TAG, "Updated Ringtone:" + preference.getKey() + " to:" + stringValue);
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_notifications_ringtone_sub_text_none);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                if (preference instanceof SwitchPreference) {
                    SwitchPreference switchPref = (SwitchPreference) preference;
                    //Log.e(TAG, "Updated Switch:" + switchPref.getKey() + " to:" + switchPref.isChecked());
                    if (preference.getKey().equals(PREF_NOTIFICATIONS_ENABLED)) {
                        //We need the inverse state, because the pref will be changing?
                        BurgerApplication.updateDeviceToken(SettingsActivity.this, !switchPref.isChecked());
                    }
                } else {
                    Log.e(TAG, "Updated Other:" + preference.getKey() + " to:" + stringValue);
                    preference.setSummary(stringValue);
                }
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has a large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        //return true;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #mBindPreferenceSummaryToValueListener
     */
    private void bindPreferenceSummaryToValue(Preference preference, boolean updateNow) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(mBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (updateNow) {
            mBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "Selected item:" + item.toString());
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || AboutPreferenceFragment.class.getName().equals(fragmentName);


    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            ((SettingsActivity) getActivity()).bindPreferenceSummaryToValue(findPreference(PREF_NOTIFICATIONS_ENABLED), false);
            ((SettingsActivity) getActivity()).bindPreferenceSummaryToValue(findPreference(PREF_NOTIFICATIONS_RINGTONE), true);
            ((SettingsActivity) getActivity()).bindPreferenceSummaryToValue(findPreference(PREF_NOTIFICATIONS_VIBRATE), false);
            ((SettingsActivity) getActivity()).bindPreferenceSummaryToValue(findPreference(PREF_NOTIFICATIONS_LED), false);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows version preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AboutPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            ((SettingsActivity) getActivity()).bindPreferenceSummaryToValue(findPreference(PREF_APP_VERSION), true);
            ((SettingsActivity) getActivity()).bindPreferenceSummaryToValue(findPreference(PREF_APP_BUILD_FLAVOR), true);


        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
