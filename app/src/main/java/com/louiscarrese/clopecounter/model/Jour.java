package com.louiscarrese.clopecounter.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by loule on 17/07/2015.
 */
public class Jour extends RealmObject {

    private long id;

    private Date date;

    private int nbClopes;
    private float avg7;
    private float avg7Predict;

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

    public int getNbClopes() {
        return nbClopes;
    }

    public void setNbClopes(int nbClopes) {
        this.nbClopes = nbClopes;
    }

    public float getAvg7() {
        return avg7;
    }

    public void setAvg7(float avg7) {
        this.avg7 = avg7;
    }

    public float getAvg7Predict() {
        return avg7Predict;
    }

    public void setAvg7Predict(float avg7Predict) {
        this.avg7Predict = avg7Predict;
    }
}
