package com.louiscarrese.clopecounter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.louiscarrese.clopecounter.business.JourBusiness;
import com.louiscarrese.clopecounter.preferences.NumberPickerPreference;
import com.louiscarrese.clopecounter.preferences.TimePickerPreference;

/**
 * Created by loule on 06/08/2015.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.activity_settings);

        //Set the summaries
        setEndDaySummary();
        setPurgeSummary();

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        TimePickerPreference endDayPref = (TimePickerPreference) findPreference("endday_timepicker");
        NumberPickerPreference purgePref = (NumberPickerPreference) findPreference("purge_delay");

        if(key.equals(endDayPref.getHourKey())) {
            JourBusiness.getInstance().setEndDayHour(sharedPreferences.getInt(key, 4));
            setEndDaySummary();
        } else if(key.equals(endDayPref.getMinuteKey())) {
            setEndDaySummary();
            JourBusiness.getInstance().setEndDayMinute(sharedPreferences.getInt(key, 4));
        } else if(key.equals(purgePref.getKey())) {
            JourBusiness.getInstance().setPurgeDelay(sharedPreferences.getInt(key, 40));
            setPurgeSummary();
        }
    }

    private void setEndDaySummary() {
        //The template
        String summaryTemplate = getString(R.string.endday_timepicker_summary);

        //Needed objects
        TimePickerPreference endDayPref = (TimePickerPreference) findPreference("endday_timepicker");
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        //The values
        int hour = sp.getInt(endDayPref.getHourKey(), 4);
        int minute = sp.getInt(endDayPref.getMinuteKey(), 0);

        endDayPref.setSummary(String.format(summaryTemplate, hour, minute));
    }

    private void setPurgeSummary() {
        //The template
        String summaryTemplate = getString(R.string.purge_numberpicker_summary);

        //Needed objects
        NumberPickerPreference purgePref = (NumberPickerPreference) findPreference("purge_delay");
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();

        purgePref.setSummary(String.format(summaryTemplate, sp.getInt(purgePref.getKey(), 40)));

    }

}