package com.pangaea.taskflow.ui.tasks.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Task;

import java.text.DateFormat;
import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<Task> {
    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tvName);
        // Populate the data into the template view using the data object
        tvName.setText(task.name);
        // Return the completed view to render on screen

        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        // Populate the data into the template view using the data object
        tvStatus.setText(task.status.name());
        switch(task.status){
            case ACTIVE:
                tvStatus.setTextColor(Color.parseColor("#00ff00"));
                tvStatus.setPaintFlags(tvStatus.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvName.setPaintFlags(tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                break;
            case INACTIVE:
                tvStatus.setTextColor(Color.parseColor("#ff0000"));
                tvStatus.setPaintFlags(tvStatus.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvName.setPaintFlags(tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                break;
            case COMPLETED:
                tvStatus.setTextColor(Color.parseColor("#c0c0c0"));
                tvStatus.setPaintFlags(tvStatus.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvName.setPaintFlags(tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }

        // Display date time metadata
        DateFormat df = DateFormat.getDateTimeInstance();
        TextView tvCreatedAt = convertView.findViewById(R.id.tvCreatedAt);
        tvCreatedAt.setText(df.format(task.createdAt));
        TextView tvLastMod = convertView.findViewById(R.id.tvLastModified);
        tvLastMod.setText(df.format(task.modifiedAt));

        return convertView;
    }
}