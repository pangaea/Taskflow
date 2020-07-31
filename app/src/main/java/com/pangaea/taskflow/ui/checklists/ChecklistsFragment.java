package com.pangaea.taskflow.ui.checklists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.ui.checklists.adapters.ChecklistsAdapter;
import com.pangaea.taskflow.ui.checklists.viewmodels.ChecklistsViewModel;
import com.pangaea.taskflow.ui.shared.ItemsFragment;

import java.util.ArrayList;
import java.util.List;

public class ChecklistsFragment extends ItemsFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        final ListView lv = view.findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle list select
                Checklist checklist = (Checklist)lv.getAdapter().getItem(position);
                navigateToItemActivity(getActivity(), ChecklistActivity.class, checklist.id);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.button_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToItemActivity(getActivity(), ChecklistActivity.class);
            }
        });

        final ChecklistsViewModel model = ViewModelProviders.of(this).get(ChecklistsViewModel.class);
        subscribeToModel(model, view);
        return view;
    }

    private void subscribeToModel(final ChecklistsViewModel model, final View view) {
        Integer project_id = getProjectId();
        LiveData<List<Checklist>> ldChecklists = (project_id != null) ? model.getChecklistsByProject(project_id) : model.getGlobalChecklists();
        ldChecklists.observe(this.getViewLifecycleOwner(), new Observer<List<Checklist>>() {
            @Override
            public void onChanged(@Nullable List<Checklist> data) {
                ListView lv = view.findViewById(R.id.listView);
                ChecklistsAdapter adapter = new ChecklistsAdapter(getActivity().getApplicationContext(), (ArrayList)data);
                lv.setAdapter(adapter);
            }
        });
        displayProjectInTitlebar(model);
    }
}