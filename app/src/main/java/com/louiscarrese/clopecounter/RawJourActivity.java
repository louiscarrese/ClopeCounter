package com.louiscarrese.clopecounter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.louiscarrese.clopecounter.model.Jour;
import com.louiscarrese.clopecounter.business.JourBusiness;


public class RawJourActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_jour);

        populateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_raw_jour, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void populateListView() {
        //Récupération de la liste des jours
        JourBusiness business = new JourBusiness(this);
        Jour[] jours = business.getAll();

        //Conversion en String (puisque Realm ne peut pas le faire tout seul)
        String[] jourStrings = new String[jours.length];
        for(int i = 0; i < jours.length; i++) {
            jourStrings[i] = business.jourToString(jours[i]);
        }

        //Création de l'adapter
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jourStrings);

        //Remplissage de la ListView
        ListView listView = (ListView) findViewById(R.id.raw_jour_list);
        listView.setAdapter(adapter);

    }
}
