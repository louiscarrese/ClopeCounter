package com.louiscarrese.clopecounter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.louiscarrese.clopecounter.R;
import com.louiscarrese.clopecounter.model.Clope;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loule on 05/08/2015.
 */
public class ClopeAdapter extends ArrayAdapter<Clope> {
    public ClopeAdapter(Context context, List<Clope> clopes) {
        super(context, 0, clopes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Clope clope = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.clope_list, parent, false);
        }
        // Lookup view for data population
        TextView clopeDate = (TextView) convertView.findViewById(R.id.clope_date);
        // Populate the data into the template view using the data object
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        clopeDate.setText(sdf.format(clope.getDate()));
        // Return the completed view to render on screen
        return convertView;
    }
}