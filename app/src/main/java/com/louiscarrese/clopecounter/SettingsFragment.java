package com.louiscarrese.clopecounter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.louiscarrese.clopecounter.business.JourBusiness;

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        TimePickerPreference endDayPref = (TimePickerPreference) findPreference("endday_timepicker");

        if(key.equals(endDayPref.getHourKey())) {
            JourBusiness.getInstance().setEndDayHour(sharedPreferences.getInt(key, 4));
        } else if(key.equals(endDayPref.getMinuteKey())) {
            JourBusiness.getInstance().setEndDayMinute(sharedPreferences.getInt(key, 4));
        }
    }
}