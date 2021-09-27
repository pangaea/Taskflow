package com.pangaea.taskflow.ui.checklists.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Checklist;

import java.text.DateFormat;
import java.util.ArrayList;

public class ChecklistsAdapter extends ArrayAdapter<Checklist> {
    public ChecklistsAdapter(Context context, ArrayList<Checklist> checklists) {
        super(context, 0, checklists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Checklist checklist = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_checklist, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tvName);
        // Populate the data into the template view using the data object
        tvName.setText(checklist.name);
        // Return the completed view to render on screen

        // Display date time metadata
        DateFormat df = DateFormat.getDateTimeInstance();
        TextView tvCreatedAt = convertView.findViewById(R.id.tvCreatedAt);
        tvCreatedAt.setText(df.format(checklist.createdAt));
        TextView tvLastMod = convertView.findViewById(R.id.tvLastModified);
        tvLastMod.setText(df.format(checklist.modifiedAt));
        return convertView;
    }
}