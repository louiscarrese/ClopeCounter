package com.louiscarrese.clopecounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.louiscarrese.clopecounter.adapter.JourAdapter;
import com.louiscarrese.clopecounter.business.JourBusiness;
import com.louiscarrese.clopecounter.model.Jour;

import java.util.List;


public class ListJourActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jours);

        populateListView();
    }

    private void populateListView() {
        //Récupération de la liste des jours
        JourBusiness business = JourBusiness.getInstance();
        List<Jour> jours = business.getAll();

        //Création de l'adapter
        JourAdapter adapter = new JourAdapter(this, jours);

        //Remplissage de la ListView
        View header = getLayoutInflater().inflate(R.layout.jours_list_header, null);
        ListView listView = (ListView) findViewById(R.id.raw_jour_list);
        if(listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(header, null, false);
        }
        listView.setAdapter(adapter);

    }
}
