package com.pangaea.taskflow.ui.projects;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.projects.viewmodels.ProjectViewModel;
import com.pangaea.taskflow.ui.shared.ItemActivity;

import androidx.lifecycle.ViewModelProvider;

public class ProjectActivity extends ItemActivity<Project, ProjectViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Extract project id
        final int projectId = getItemId();

        ProjectViewModel.Factory factory = new ProjectViewModel.Factory(
                getApplication(), projectId);

        final ProjectViewModel model = new ViewModelProvider(this, factory)
                .get(ProjectViewModel.class);

        subscribeToModel(model);

        if(projectId == -1)
            attachDirtyEvents(R.id.editName, R.id.editDescription);
    }

    @Override public void fillFields(Project project){
        TextView tvName = findViewById(R.id.editName);
        tvName.setText(project.name);
        TextView tvDescription = findViewById(R.id.editDescription);
        tvDescription.setText(project.description);
        attachDirtyEvents(R.id.editName, R.id.editDescription);

        Button fab = findViewById(R.id.button_project_home);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToProjectHome(getItemId());
            }
        });
    }
    @Override public Project buildModel(){
        int projectId = getItemId();
        TextView tvName = findViewById(R.id.editName);
        TextView tvDescription = findViewById(R.id.editDescription);
        Project project = new Project(tvName.getText().toString(), tvDescription.getText().toString());
        if(projectId > 0) project.id = projectId;
        return project;
    }
    @Override public String deleteWarning(){
        TextView tvName = findViewById(R.id.editName);
        return "Are you sure you want to delete the project '" + tvName.getText().toString() + "'?";

    }
}