package com.louiscarrese.clopecounter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.louiscarrese.clopecounter.business.ClopeBusiness;
import com.louiscarrese.clopecounter.model.Clope;


public class RawClopesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_clopes);

        populateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_raw_clopes, menu);
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
            //Pas d'écran de setting sur cette activity
            return true;
        } else if((id == R.id.action_generate_random_clope)) {
            generateRandomDataSet();
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateListView() {

        ClopeBusiness business = new ClopeBusiness(this);
        Clope[] clopes = business.getAll();

        //Conversion en String
        String[] clopeStrings = new String[clopes.length];
        for(int i = 0; i < clopes.length; i++) {
            clopeStrings[i] = business.clopeToString(clopes[i]);
        }

        //Création de l'adapter
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clopeStrings);

        //Remplissage de la ListView
        ListView listView = (ListView) findViewById(R.id.raw_clope_list);
        listView.setAdapter(adapter);

    }

    private void generateRandomDataSet() {
        //Business
        ClopeBusiness business = new ClopeBusiness(this);

        for(int i = 0; i < 30; i++) {
            business.addRandomClope();
        }

        populateListView();
    }
}
