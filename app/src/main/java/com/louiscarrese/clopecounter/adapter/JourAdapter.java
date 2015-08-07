package com.louiscarrese.clopecounter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.louiscarrese.clopecounter.R;
import com.louiscarrese.clopecounter.model.Jour;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by loule on 06/08/2015.
 */
public class JourAdapter extends ArrayAdapter<Jour> {
    public JourAdapter(Context context, List<Jour> jours) {
        super(context, 0, jours);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for this position
        Jour jour = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.jours_list, parent, false);
        }

        //Lookup view for data population
        TextView jourDate  = (TextView) convertView.findViewById(R.id.jour_date);
        TextView jourNb = (TextView) convertView.findViewById(R.id.jour_nbclopes);
        TextView jourAvg = (TextView) convertView.findViewById(R.id.jour_avg7);

        //Populate the data into the template view using the data object
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        jourDate.setText(sdf.format(jour.getDate()));
        jourNb.setText(String.format("%d", jour.getNbClopes()));
        jourAvg.setText(String.format("%.2f", jour.getAvg7()));
        return convertView;
    }
}
