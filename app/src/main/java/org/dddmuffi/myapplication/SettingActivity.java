package org.dddmuffi.myapplication;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class SettingActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Preference ipadressePref = findPreference(getString(R.string.preference_ipadresse_key));
        Preference portPref = findPreference(getString(R.string.preference_port_key));
        Preference vibratePref = findPreference(getString(R.string.preference_vibrate_key));

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String savedIpadresse = sharedPrefs.getString(ipadressePref.getKey(), "");
        String savedPort = sharedPrefs.getString(portPref.getKey(), "10000");
        Boolean savedVibrate = sharedPrefs.getBoolean(vibratePref.getKey(), true);

        onPreferenceChange(ipadressePref, savedIpadresse);
        onPreferenceChange(portPref, savedPort);
        onPreferenceChange(vibratePref, savedVibrate);

        ipadressePref.setOnPreferenceChangeListener(this);
        portPref.setOnPreferenceChangeListener(this);
        vibratePref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        preference.setSummary(value.toString());
        return true;
    }

}