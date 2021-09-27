package com.pangaea.taskflow.ui.tasks;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;
import com.pangaea.taskflow.ui.shared.ProjectAssociatedItemActivity;
import com.pangaea.taskflow.ui.tasks.viewmodels.TaskViewModel;

import androidx.lifecycle.ViewModelProvider;

public class TaskActivity extends ProjectAssociatedItemActivity<Task, TaskViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AutoCompleteTextView statusSpinner = findViewById(R.id.status_spinner);
        statusSpinner.setAdapter(adapter);

        // Extract task id
        final int taskId = getItemId();

        TaskViewModel.Factory factory = new TaskViewModel.Factory(
                getApplication(), taskId);

        final TaskViewModel model = new ViewModelProvider(this, factory)
                .get(TaskViewModel.class);

        // Project assign ///////////////////////////////////////////
        populateProjects(model.getProjects(), new ProjectsLoadedCallback(){
            @Override public void projectsLoaded(){
                subscribeToModel(model);
            }
        });

        if(taskId == -1)
            attachDirtyEvents(R.id.editName, R.id.editDetails, R.id.status_spinner, R.id.project_spinner);
    }

    @Override public void initNewItem() {
        AutoCompleteTextView statusSpinner = findViewById(R.id.status_spinner);
        statusSpinner.setText(TaskStatus.INACTIVE.name(), false);
    }

    @Override public void fillFields(Task task){
        TextView tvName = findViewById(R.id.editName);
        tvName.setText(task.name);
        TextView tvDetails = findViewById(R.id.editDetails);
        tvDetails.setText(task.details);

        // TODO: Remove reliance on string table and enum matching
        AutoCompleteTextView statusSpinner = findViewById(R.id.status_spinner);
        if(task.status != null)
            statusSpinner.setText(task.status.name(), false);

        // Project assign ///////////////////////////////////////////
        setProjectSelection(task.project_id);

        attachDirtyEvents(R.id.editName, R.id.editDetails, R.id.status_spinner, R.id.project_spinner);
    }
    @Override public Task buildModel(){
        int taskId = getItemId();
        TextView tvName = findViewById(R.id.editName);
        TextView tvDetails = findViewById(R.id.editDetails);

        // TODO: Remove reliance on string table and enum matching
        AutoCompleteTextView statusSpinner = findViewById(R.id.status_spinner);
        TaskStatus status = TaskStatus.valueOf(statusSpinner.getText().toString());

        Task task = new Task(tvName.getText().toString(), tvDetails.getText().toString(), status);
        if(taskId > 0) task.id = taskId;

        // Project assign ///////////////////////////////////////////
        task.project_id = getProjectId();

        return task;
    }
    @Override public String deleteWarning(){
        TextView tvName = findViewById(R.id.editName);
        String delMsg = getResources().getString(R.string.task_delete_conformation_label);
        return delMsg.replace("%1", tvName.getText().toString());
    }
}
