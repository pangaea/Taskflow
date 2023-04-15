package com.pangaea.taskflow.ui.shared.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.pangaea.taskflow.R;

import java.util.List;
import java.util.function.Consumer;

public class AutoCompleteSpinnerAdapter extends ArrayAdapter<Pair<String, String>> {

    //Context context;
    int textViewResourceId;
    int selectedPosition;
    List<Pair<String, String>> mList;

    public AutoCompleteSpinnerAdapter(Context context, int textViewResourceId,
                                      List<Pair<String, String>> mList) {
        super(context, textViewResourceId, mList);
        //this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.mList = mList;
        selectedPosition = 0;
    }

    @Override
    public Pair<String, String> getItem(int position) {
        return mList.get(position);
    }

    public Pair<String, String> getSelectedPosition() {
        return getItem(selectedPosition);
    }

    public Pair<String, String> setSelectedPosition(int position) {
        selectedPosition = position;
        return getItem(position);
    }

    public Pair<String, String> setSelectedValue(String name) {
        int position = 0;
        for (Pair<String, String> item : mList) {
            if (item.first.equals(name)) {
                return setSelectedPosition(position);
            }
            position++;
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).second.hashCode();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(textViewResourceId, parent, false);
        }

        Pair<String, String> selectedItem = mList.get(position);
        if (selectedItem != null) {
            TextView textView = (TextView) convertView.findViewById(R.id.textAutoComplete);
            if (textView != null) {
                textView.setText(selectedItem.second);
            }

        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /* STATIC METHODS */

    public static void setUpdateListener(AutoCompleteTextView autoCompleteSpinner) {
        setUpdateListener(autoCompleteSpinner, null);
    }

    public static void setUpdateListener(AutoCompleteTextView autoCompleteSpinner,
                                     Consumer<String> callback) {
        autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                AutoCompleteSpinnerAdapter da =  (AutoCompleteSpinnerAdapter)autoCompleteSpinner.getAdapter();
                Pair<String, String> selItem = da.setSelectedPosition(position);
                autoCompleteSpinner.setText(selItem.second, false);
                if (callback != null) {
                    callback.accept(selItem.first);
                }
            }
        });
    }

    public static void setSelectedPosition(AutoCompleteTextView autoCompleteSpinner, int position) {
        AutoCompleteSpinnerAdapter da =  (AutoCompleteSpinnerAdapter)autoCompleteSpinner.getAdapter();
        Pair<String, String> selItem = da.setSelectedPosition(position);
        autoCompleteSpinner.setText(selItem.second, false);
    }

    public static void setSelectedValue(AutoCompleteTextView autoCompleteSpinner, String value) {
        AutoCompleteSpinnerAdapter da =  (AutoCompleteSpinnerAdapter)autoCompleteSpinner.getAdapter();
        Pair<String, String> selItem = da.setSelectedValue(value);
        autoCompleteSpinner.setText(selItem.second, false);
    }

    public static String getSelectedSpinnerValue(AutoCompleteTextView autoCompleteSpinner) {
        AutoCompleteSpinnerAdapter da =  (AutoCompleteSpinnerAdapter)autoCompleteSpinner.getAdapter();
        Pair<String, String> selItem = da.getSelectedPosition();
        return selItem.first;
    }
}
