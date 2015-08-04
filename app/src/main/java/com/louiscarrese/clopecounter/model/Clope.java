package com.louiscarrese.clopecounter.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by loule on 17/07/2015.
 */
public class Clope extends RealmObject {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
