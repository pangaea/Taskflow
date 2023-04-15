package com.pangaea.taskflow.ui.projects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.pangaea.taskflow.ui.shared.adapters.AutoCompleteSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        //////////////////////////////////
        setupToolbar(getActivity(), view, null, false,
                o -> {subscribeToModel(model, view);});
        /////////////////////////////////////////////////////

        subscribeToModel(model, view);
        return view;
    }

    private void subscribeToModel(final ProjectsViewModel model, final View view) {

        // Get 'sortBy' from dropdown
        AutoCompleteTextView sortSpinner = view.findViewById(R.id.sort_spinner);
        String sortBy = AutoCompleteSpinnerAdapter.getSelectedSpinnerValue(sortSpinner);

        model.getAllProjects().observe(this.getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> data) {
                // Sort here instead of db?
                if (sortBy.equals("NAME")) {
                    data = data.stream().sorted((o1, o2) -> o1.name.compareTo(o2.name))
                            .collect(Collectors.toList());
                } else if (sortBy.equals("CREATED")) {
                    data = data.stream().sorted((o1, o2) -> Math.negateExact(o1.createdAt.compareTo(o2.createdAt)))
                            .collect(Collectors.toList());
                } else if (sortBy.equals("MODIFIED")) {
                    data = data.stream().sorted((o1, o2) -> Math.negateExact(o1.modifiedAt.compareTo(o2.modifiedAt)))
                            .collect(Collectors.toList());
                }

                ListView lv = view.findViewById(R.id.listView);
                ProjectsAdapter adapter = new ProjectsAdapter(getActivity().getApplicationContext(), (ArrayList)data);
                lv.setAdapter(adapter);
            }
        });

        // Hide project picket
        com.google.android.material.textfield.TextInputLayout projectPickerLayout =
                getActivity().findViewById(R.id.project_picker_layout);
        projectPickerLayout.setVisibility(View.GONE);
    }
}