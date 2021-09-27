package com.pangaea.taskflow.ui.shared;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.entities.BaseEntity;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public abstract class ProjectAssociatedItemActivity<M, VM extends ItemViewModel> extends ItemActivity<M, VM> {
    final List<Project> projects = new ArrayList();
    static String noSelection = TaskflowApp.getRes().getString(R.string.no_project_selected);

    public interface ProjectsLoadedCallback{
        void projectsLoaded();
    }

    protected void populateProjects(LiveData<List<Project>> projectReq, ProjectsLoadedCallback callback) {

        final Context self = this;
        final ProjectsLoadedCallback callbackParam = callback;
        projectReq.observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> data) {
                // Insert projects into dropdown
                List<String> z = new ArrayList();
                z.add(noSelection);
                for(Project p : data){
                    projects.add(p);
                    z.add(p.name);
                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, z);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                AutoCompleteTextView projectSpinner = findViewById(R.id.project_spinner);
                projectSpinner.setAdapter(adapter);
                Integer project_id = getCurrentProjectId();
                setProjectSelection(project_id);

                callbackParam.projectsLoaded();
            }
        });
    }

    protected void setProjectSelection(Integer projectId){
        if(projectId != null) {
            AutoCompleteTextView projectSpinner = findViewById(R.id.project_spinner);
            for (int i = 0; i < projects.size(); i++) {
                Project p = projects.get(i);
                if (p.id == projectId) {
                    projectSpinner.setText(p.name, false);
                    break;
                }
            }
        }
        else{
            AutoCompleteTextView projectSpinner = findViewById(R.id.project_spinner);
            projectSpinner.setText(noSelection, false);
        }
    }

    protected Integer getProjectId(){
        AutoCompleteTextView projectSpinner = findViewById(R.id.project_spinner);
        String pName = projectSpinner.getText().toString();
        for(int i = 0; i < projects.size(); i++){
            Project p = projects.get(i);
            if(p.name.equals(pName)) {
                return p.id;
            }
        }
        return null;
    }
}
