package com.louiscarrese.clopecounter.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by loule on 17/07/2015.
 */
public class Clope extends RealmObject {

    private long id;
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
