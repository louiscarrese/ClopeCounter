package com.louiscarrese.clopecounter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.louiscarrese.clopecounter.business.ClopeBusiness;
import com.louiscarrese.clopecounter.model.Jour;
import com.louiscarrese.clopecounter.business.JourBusiness;
import com.louiscarrese.clopecounter.model.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Handle the Realm configuration
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this.getApplicationContext())
                .name("default.realm")
                .schemaVersion(1) //TODO: Mettre ça quelque part en conf peut être ?
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        try {
            Realm realm = Realm.getInstance(realmConfig);
            realm.close();
        } catch(Exception e) {
            Log.e("MainActivity", "Error getting realm", e);
            return;
        }

        refreshCounters();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:

                //TODO: Faire l'écran de settings
                return true;
            case R.id.action_raw_clope:
                Intent rawClopesIntent = new Intent(this, RawClopesActivity.class);
                startActivity(rawClopesIntent);
                return true;
            case R.id.action_raw_jour:
                Intent rawJourIntent = new Intent(this, RawJourActivity.class);
                startActivity(rawJourIntent);
                return true;
            case R.id.action_rebuild_stats:
                refreshStats(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void addClope(View view) {
        JourBusiness business = new JourBusiness(this);

        Jour jour = business.addClope();

        refreshCounters(jour);
    }

    public void refreshStats(View view) {
        JourBusiness business = new JourBusiness(this);

        business.refreshStats();

        refreshCounters(business.getToday());
    }

    public void addRandomClope(View view) {
        ClopeBusiness business = new ClopeBusiness(this);

        business.addRandomClope();

    }

    private void refreshCounters() {
        JourBusiness utils = new JourBusiness(this);

        Jour jour = utils.getToday();

        refreshCounters(jour);
    }
    private void refreshCounters(Jour jour) {
        TextView todayValue = (TextView)findViewById(R.id.today_value);
        TextView avg7Value = (TextView)findViewById(R.id.avg7_value);
        TextView avg7PredictValue = (TextView)findViewById(R.id.avg7_predict_value);

        todayValue.setText(String.format("%d", jour.getNbClopes()));
        avg7Value.setText(String.format("%.2g", jour.getAvg7()));
        avg7PredictValue.setText(String.format("%.2g", jour.getAvg7Predict()));

        if(jour.getNbClopes() > jour.getAvg7()) {
            ((TextView)findViewById(R.id.today_value)).setTextColor(Color.RED);
        }


    }
}
