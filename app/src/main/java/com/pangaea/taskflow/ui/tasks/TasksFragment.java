package com.pangaea.taskflow.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.ui.shared.ItemsFragment;
import com.pangaea.taskflow.ui.tasks.adapters.TasksAdapter;
import com.pangaea.taskflow.ui.tasks.viewmodels.TasksViewModel;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends ItemsFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        final ListView lv = view.findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle list select
                Task task = (Task)lv.getAdapter().getItem(position);
                navigateToItemActivity(getActivity(), TaskActivity.class, task.id);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.button_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToItemActivity(getActivity(), TaskActivity.class);
            }
        });

        final TasksViewModel model = ViewModelProviders.of(this).get(TasksViewModel.class);

        //////////////////////////////////
        setupToolbar(getActivity(), view, List.of("None", "Complete", "Active"),
                List.of("Modified", "Created", "Name"),
                o -> {subscribeToModel(model, view);});
        /////////////////////////////////////////////////////

        //final TasksViewModel model = ViewModelProviders.of(this).get(TasksViewModel.class);
        subscribeToModel(model, view);
        return view;
    }

    private void subscribeToModel(final TasksViewModel model, final View view) {
        Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();

        // Get 'sortBy' from dropdown
        AutoCompleteTextView sortSpinner = view.findViewById(R.id.sort_spinner);
        String sortBy = sortSpinner.getText().toString();

        LiveData<List<Task>> ldTasks = (project_id != null) ? model.getTasksByProject(project_id, sortBy) :
                model.getGlobalTasks(sortBy);
        ldTasks.observe(this.getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> data) {
                ListView lv = view.findViewById(R.id.listView);
                TasksAdapter adapter = new TasksAdapter(getActivity().getApplicationContext(), (ArrayList)data);
                lv.setAdapter(adapter);
            }
        });

        displayProjectInTitlebar(model);
    }
}