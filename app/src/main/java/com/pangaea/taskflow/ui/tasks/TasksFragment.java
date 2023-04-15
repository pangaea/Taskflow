package com.pangaea.taskflow.ui.tasks;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;
import com.pangaea.taskflow.ui.shared.ItemsFragment;
import com.pangaea.taskflow.ui.shared.adapters.AutoCompleteSpinnerAdapter;
import com.pangaea.taskflow.ui.tasks.adapters.TasksAdapter;
import com.pangaea.taskflow.ui.tasks.enums.TaskStatusDisplayEnum;
import com.pangaea.taskflow.ui.tasks.viewmodels.TasksViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Pair<String, String>> statusOptions = new ArrayList<Pair<String, String>>();
        statusOptions.add(new Pair<String, String>("NONE", getResources().getString(R.string.No_Filter)));
        statusOptions.addAll(TaskStatusDisplayEnum.spinnerData(getContext()));
        setupToolbar(getActivity(), view, statusOptions, true,
                o -> {subscribeToModel(model, view);});
        /////////////////////////////////////////////////////

        subscribeToModel(model, view);
        return view;
    }

    private void subscribeToModel(final TasksViewModel model, final View view) {
        Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();

        // Get 'sortBy' from dropdown
        AutoCompleteTextView sortSpinner = view.findViewById(R.id.sort_spinner);
        String sortBy = AutoCompleteSpinnerAdapter.getSelectedSpinnerValue(sortSpinner);

        AutoCompleteTextView filterSpinner = view.findViewById(R.id.filter_spinner);
        String filterBy = AutoCompleteSpinnerAdapter.getSelectedSpinnerValue(filterSpinner);

        LiveData<List<Task>> ldTasks = (project_id != null) ? model.getTasksByProject(project_id) :
                model.getGlobalTasks();
        ldTasks.observe(this.getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> data) {
                ListView lv = view.findViewById(R.id.listView);

                // Filter here instead of db?
                TaskStatus taskStatus = TaskStatus.lookup(filterBy);
                if (taskStatus != null) {
                    data = data.stream().filter(o -> o.status == taskStatus)
                            .collect(Collectors.toList());
                }

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
                } else if (sortBy.equals("STATUS")) {
                    data = data.stream().sorted((o1, o2) -> o1.status.compareTo(o2.status))
                            .collect(Collectors.toList());
                }

                TasksAdapter adapter = new TasksAdapter(getActivity().getApplicationContext(), (ArrayList)data);
                lv.setAdapter(adapter);
            }
        });

        displayProjectInTitlebar(model);
    }
}