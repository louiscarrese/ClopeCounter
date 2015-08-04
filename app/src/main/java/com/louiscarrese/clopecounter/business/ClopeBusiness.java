package com.louiscarrese.clopecounter.business;

import android.content.Context;

import com.louiscarrese.clopecounter.model.Clope;
import com.louiscarrese.clopecounter.model.Jour;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by loule on 18/07/2015.
 */
public class ClopeBusiness {
    private Context context;

    public ClopeBusiness(Context ctx) {
        this.context = ctx.getApplicationContext();
    }

    public String clopeToString(Clope c) {
        return c.getDate().toString();

    }

    public Clope[] getAll() {

        Realm realm = Realm.getInstance(this.context);

        RealmQuery<Clope> query = realm.where(Clope.class);

        RealmResults<Clope> results = query.findAllSorted("date");

        return results.toArray(new Clope[0]);
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

        Clope c = new Clope();
        c.setDate(cal.getTime());

        Realm realm = Realm.getInstance(this.context);
        realm.beginTransaction();
        realm.copyToRealm(c);
        realm.commitTransaction();

        return c;
    }
}
