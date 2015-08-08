package com.louiscarrese.clopecounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.louiscarrese.clopecounter.adapter.ClopeAdapter;
import com.louiscarrese.clopecounter.business.ClopeBusiness;
import com.louiscarrese.clopecounter.model.Clope;

import java.util.List;


public class ListClopesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clopes);

        populateListView();

        //Default result :
        setResult(RESULT_OK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_clopes, menu);
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
                deleteClope(clope);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void populateListView() {

        ClopeBusiness business = ClopeBusiness.getInstance();
        List<Clope> clopes = business.getAll();

        //Création de l'adapter
        ClopeAdapter adapter = new ClopeAdapter(this.getApplicationContext(), clopes);

        //Remplissage de la ListView
        View header = getLayoutInflater().inflate(R.layout.clope_list_header, null);
        ListView listView = (ListView) findViewById(R.id.raw_clope_list);
        if(listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(header, null, false);
        }
        listView.setAdapter(adapter);

        //Menu contextuel pour supprimer une clope
        registerForContextMenu(listView);

    }

    private void generateRandomDataSet() {
        //Business
        ClopeBusiness business = ClopeBusiness.getInstance();

        for(int i = 0; i < 30; i++) {
            business.addRandomClope();
        }

        populateListView();
        setResult(MainActivity.RESULT_REFRESH);
    }

    private void deleteClope(Clope clope) {
        ClopeBusiness business = ClopeBusiness.getInstance();

        business.delete(clope);

        populateListView();
        setResult(MainActivity.RESULT_REFRESH);
    }
}
