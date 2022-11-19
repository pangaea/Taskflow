package com.pangaea.taskflow.ui.shared;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.notes.NoteActivity;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

public class ItemsFragment extends Fragment {
    protected void navigateToItemActivity(Context context, Class<?> clazz) {
        navigateToItemActivity(context, clazz, null);
    }
    protected void navigateToItemActivity(Context context, Class<?> clazz, Integer id) {
        Intent intent = new Intent(context, clazz);
        if(id != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            intent.putExtras(bundle);
        }
        else{
            //Integer project_id = ((TaskflowApp)getActivity().getApplication()).getCurrentProjectId(getActivity());
            Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();
            if(project_id != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("project_id", project_id);
                intent.putExtras(bundle);
            }
        }
        startActivity(intent);
    }

    protected void displayProjectInTitlebar(ItemsViewModel model) {
        // Re-show project picket
        com.google.android.material.textfield.TextInputLayout projectPickerLayout =
                getActivity().findViewById(R.id.project_picker_layout);
        projectPickerLayout.setVisibility(View.VISIBLE);

        //Integer project_id = ((TaskflowApp)getActivity().getApplication()).getCurrentProjectId(getActivity());
        Integer project_id = ((BaseActivity) getActivity()).getCurrentProjectId();
        if (project_id != null) {
            model.getProject(project_id).observe(this.getViewLifecycleOwner(), new Observer<List<Project>>() {
                @Override
                public void onChanged(@Nullable List<Project> data) {
                    Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                    CharSequence curTitle = toolbar.getTitle();
                    toolbar.setTitle(curTitle + ": " + data.get(0).name);
                }
            });
        }
    }

    protected void onFilterChange(String filter) {
    }

    protected void onSortChange(String filter) {
    }

    protected void setupToolbar(FragmentActivity frag, View view, List<String> filters, List<String> sorts,
                                Consumer<String> sortCallback) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(frag, R.layout.project_spinner_item, sorts);
        adapter.setDropDownViewResource(R.layout.project_spinner_dropdown_item);
        AutoCompleteTextView sortSpinner = view.findViewById(R.id.sort_spinner);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setText(sorts.get(0), false);
        sortSpinner.addTextChangedListener( new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sortCallback.accept(s.toString());
                //onSortChange(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
//
//        ArrayAdapter<String> adapter2 =
//                new ArrayAdapter<>(getActivity(), R.layout.project_spinner_item, filters);
//        adapter.setDropDownViewResource(R.layout.project_spinner_dropdown_item);
//        AutoCompleteTextView filterSpinner = view.findViewById(R.id.filter_spinner);
//        filterSpinner.setAdapter(adapter2);
//        filterSpinner.setText(filters.get(0), false);
//        filterSpinner.addTextChangedListener( new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                onFilterChange(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }
}
