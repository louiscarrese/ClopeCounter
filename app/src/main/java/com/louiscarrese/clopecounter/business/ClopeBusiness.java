package com.louiscarrese.clopecounter.business;

import android.content.Context;

import com.louiscarrese.clopecounter.model.Clope;
import com.louiscarrese.clopecounter.model.Jour;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by loule on 18/07/2015.
 */
public class ClopeBusiness {

    /** Singleton implementation */
    private static ClopeBusiness instance = null;

    private ClopeBusiness() {
    }

    public static ClopeBusiness getInstance() {
        if(instance == null) {
            instance = new ClopeBusiness();
        }
        return instance;
    }

    public String clopeToString(Clope c) {
        return c.getId() + " - " + c.getDate().toString();

    }

    public Clope createClope() {
        Realm realm = Realm.getDefaultInstance();

        long id = realm.where(Clope.class).maximumInt("id") + 1;

        Clope c = new Clope();
        c.setId(id);

        return c;
    }

    public List<Clope> getAll() {

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Clope> query = realm.where(Clope.class);

        RealmResults<Clope> results = query.findAllSorted("date");

        return results.subList(0, results.size());
    }

    public void delete(Clope clope) {
        JourBusiness jourBusiness = JourBusiness.getInstance();
        Jour jour = jourBusiness.getJourFromClope(clope);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.where(Clope.class).equalTo("id", clope.getId()).findAll().clear();
        jourBusiness.refreshStats(jour);
        realm.commitTransaction();

    }

    public Clope addRandomClope() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());

        int dayOffset = (int)(Math.random() * -10);
        int hour = (int)(Math.random() * 24);
        int minute = (int)(Math.random() * 60);
        int second = (int)(Math.random() * 60);

        cal.add(Calendar.DAY_OF_YEAR, dayOffset);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);

        Clope c = createClope();
        c.setDate(cal.getTime());

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(c);
        realm.commitTransaction();

        return c;
    }
}
