package com.louiscarrese.clopecounter.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.internal.ColumnType;
import io.realm.internal.Table;

/**
 * Created by loule on 04/08/2015.
 */
public class Migration implements RealmMigration {

    @Override
    public long execute(Realm realm, long version) {
        //v0 => v1: add Clope.id and Jour.id
        if(version == 0) {
            Log.d("Migration", "Migrating from v0");
            //Clope
            Table clopeTable = realm.getTable(Clope.class);
            //Let's make sure the column is not already there...
            long clopeIdIndex = getIndexForProperty(clopeTable, "id");
            if(clopeIdIndex == -1) {
                clopeIdIndex = clopeTable.addColumn(ColumnType.INTEGER, "id");
                for (int i = 0; i < clopeTable.size(); i++) {
                    clopeTable.setLong(clopeIdIndex, i, i);
                }
            }

            //Jour
            Table jourTable = realm.getTable(Jour.class);
            //Let's make sure the column is not already there...
            long jourIdIndex = getIndexForProperty(jourTable, "id");
            if(jourIdIndex == -1) {
                jourIdIndex = jourTable.addColumn(ColumnType.INTEGER, "id");
                for(int i = 0; i < jourTable.size(); i++) {
                    jourTable.setLong(jourIdIndex, i, i);
                }
            }

            version++;
        }

        return version;
    }

    private long getIndexForProperty(Table table, String propertyName) {
        for(int i = 0; i < table.getColumnCount(); i++) {
            if(table.getColumnName(i).equals(propertyName)) {
                return i;
            }
        }

        return -1;
    }
}
