package com.louiscarrese.clopecounter;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

import com.louiscarrese.clopecounter.business.JourBusiness;
import com.louiscarrese.clopecounter.model.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by loule on 07/08/2015.
 */
public class ClopeCounterApplication extends Application implements OnSharedPreferenceChangeListener{

    @Override
    public void onCreate() {
        super.onCreate();

        //Handle the Realm configuration
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this.getApplicationContext())
                .name("default.realm")
                .schemaVersion(1) //TODO: Mettre ça quelque part en conf peut être ?
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        try {
            Realm realm = Realm.getInstance(realmConfig);
            realm.close();
        } catch(Exception e) {
            Log.e("MainActivity", "Error getting realm", e);
            return;
        }


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //Initialisation des préférences
        JourBusiness jourBusiness = JourBusiness.getInstance();
        jourBusiness.setEndDayHour(sp.getInt(getString(R.string.endday_hour_key), 4));
        jourBusiness.setEndDayMinute(sp.getInt(getString(R.string.endday_minute_key), 0));

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.endday_hour_key))) {
            JourBusiness.getInstance().setEndDayHour(sharedPreferences.getInt(key, 4));
        } else if(key.equals(getString(R.string.endday_minute_key))) {
            JourBusiness.getInstance().setEndDayMinute(sharedPreferences.getInt(key, 0));
        }
    }
}
