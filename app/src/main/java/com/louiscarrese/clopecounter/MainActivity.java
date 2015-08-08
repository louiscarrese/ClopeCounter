package com.louiscarrese.clopecounter;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.louiscarrese.clopecounter.business.JourBusiness;
import com.louiscarrese.clopecounter.model.Jour;


public class MainActivity extends Activity {

    private static final int REQUEST_CODE_CLOPE_LIST = 1;
    private static final int REQUEST_CODE_JOUR_LIST = 2;

    static final int RESULT_REFRESH = RESULT_FIRST_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_raw_clope:
                Intent rawClopesIntent = new Intent(this, ListClopesActivity.class);
                startActivityForResult(rawClopesIntent, REQUEST_CODE_CLOPE_LIST);
                return true;
            case R.id.action_raw_jour:
                Intent rawJourIntent = new Intent(this, ListJourActivity.class);
                startActivityForResult(rawJourIntent, REQUEST_CODE_JOUR_LIST);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CLOPE_LIST:
            case REQUEST_CODE_JOUR_LIST:
                if(resultCode == RESULT_REFRESH) {
                    refreshCounters();
                }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCounters();
    }

    public void addClope(View view) {
        JourBusiness business = JourBusiness.getInstance();

        business.addClope();

        refreshCounters();
    }

    private void refreshStats() {
        JourBusiness business = JourBusiness.getInstance();

        business.refreshStats();

        refreshCounters();
    }

    private void refreshCounters() {
        JourBusiness utils = JourBusiness.getInstance();

        Jour jour = utils.getToday();

        refreshCounters(jour);
    }
    private void refreshCounters(Jour jour) {

        //Refresh counters on the main activity
        TextView todayValue = (TextView)findViewById(R.id.today_value);
        TextView avg7Value = (TextView)findViewById(R.id.avg7_value);
        TextView avg7PredictValue = (TextView)findViewById(R.id.avg7_predict_value);

        todayValue.setText(String.format("%d", jour.getNbClopes()));
        avg7Value.setText(String.format("%.2f", jour.getAvg7()));
        avg7PredictValue.setText(String.format("%.2f", jour.getAvg7Predict()));

        if(jour.getNbClopes() > jour.getAvg7()) {
            ((TextView)findViewById(R.id.today_value)).setTextColor(Color.RED);
        }


        //Schedule a refresh of the widget
        Intent intent = new Intent(this,ClopeCounterAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intent);

    }
}
