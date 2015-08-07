package com.louiscarrese.clopecounter;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.louiscarrese.clopecounter.business.JourBusiness;
import com.louiscarrese.clopecounter.model.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by loule on 07/08/2015.
 */
public class ClopeCounterApplication extends Application {

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
        jourBusiness.setPurgeDelay(sp.getInt(getString(R.string.purge_delay_key), 40));

    }

}
