package com.louiscarrese.clopecounter;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.louiscarrese.clopecounter.adapter.ClopeAdapter;
import com.louiscarrese.clopecounter.business.ClopeBusiness;
import com.louiscarrese.clopecounter.model.Clope;

import java.util.List;


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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ctx_raw_clope, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_ctx_clope_delete:
                ListView listView = (ListView) findViewById(R.id.raw_clope_list);
                Clope clope = (Clope)(listView.getAdapter().getItem(info.position));
                deleteClope(clope.getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void populateListView() {

        ClopeBusiness business = new ClopeBusiness(this);
        List<Clope> clopes = business.getAll();

        //Création de l'adapter
        ClopeAdapter adapter = new ClopeAdapter(this.getApplicationContext(), clopes);

        //Remplissage de la ListView
        ListView listView = (ListView) findViewById(R.id.raw_clope_list);
        listView.setAdapter(adapter);

        //Menu contextuel pour supprimer une clope
        registerForContextMenu(listView);

    }

    private void generateRandomDataSet() {
        //Business
        ClopeBusiness business = new ClopeBusiness(this);

        for(int i = 0; i < 30; i++) {
            business.addRandomClope();
        }

        populateListView();
    }

    private void deleteClope(long id) {
        ClopeBusiness business = new ClopeBusiness(this);

        business.delete(id);

        populateListView();

        Log.d("verbose : ", "clope id a deleter : " + id);
    }
}
