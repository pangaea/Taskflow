package com.pangaea.taskflow.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.projects.adapters.ProjectsAdapter;
import com.pangaea.taskflow.ui.projects.viewmodels.ProjectsViewModel;
import com.pangaea.taskflow.ui.shared.ItemsFragment;

import java.util.ArrayList;
import java.util.List;

public class ProjectsFragment extends ItemsFragment {

    private ProjectsViewModel projectsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        final ListView lv = view.findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle list select
                Project project = (Project)lv.getAdapter().getItem(position);
                navigateToItemActivity(getActivity(), ProjectActivity.class, project.id);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.button_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToItemActivity(getActivity(), ProjectActivity.class);
            }
        });

        final ProjectsViewModel model = ViewModelProviders.of(this).get(ProjectsViewModel.class);
        subscribeToModel(model, view);
        return view;
    }

    private void subscribeToModel(final ProjectsViewModel model, final View view) {
        model.getAllProjects().observe(this.getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> data) {
                ListView lv = view.findViewById(R.id.listView);
                ProjectsAdapter adapter = new ProjectsAdapter(getActivity().getApplicationContext(), (ArrayList)data);
                lv.setAdapter(adapter);
            }
        });
    }
}