package com.pangaea.taskflow.ui.shared;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public abstract class ProjectAssociatedItemActivity<M, VM extends ItemViewModel> extends ItemActivity<M, VM> {
    final List<Project> projects = new ArrayList();
    static String noSelection = "--None--";

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

                int project_id = -1;
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                if(bundle != null) {
                    project_id = bundle.getInt("project_id", -1);
                    //if( project_id > 0) {
                    //setProjectSelection(bundle.getInt("project_id", -1));
                    //}
                }
                setProjectSelection(project_id);

                callbackParam.projectsLoaded();
            }
        });
    }

    protected void setProjectSelection(Integer projectId){
        if(projectId != null && projectId > 0) {
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
