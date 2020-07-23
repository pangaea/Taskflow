package com.pangaea.taskflow.ui.notes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Note;

import java.util.ArrayList;

public class NotesAdapter extends ArrayAdapter<Note> {
    public NotesAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        // Populate the data into the template view using the data object
        tvTitle.setText(note.title);
        // Return the completed view to render on screen
        return convertView;
    }
}