package com.pangaea.taskflow.ui.shared;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.entities.BaseEntity;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.shared.adapters.AutoCompleteSpinnerAdapter;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;
import com.pangaea.taskflow.ui.tasks.enums.TaskStatusDisplayEnum;

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
                if (data == null) return;

                // Insert projects into dropdown
                List<Pair<String, String>> projects = new ArrayList<Pair<String, String>>();
                projects.add(new Pair<String, String>("0", noSelection));
                for (Project p : data) {
                    projects.add(new Pair<String, String>(String.valueOf(p.id), p.name));
                }

                // Create status spinner
                AutoCompleteSpinnerAdapter adapter = new AutoCompleteSpinnerAdapter(self,
                        R.layout.project_spinner_item, projects);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                AutoCompleteTextView statusSpinner = findViewById(R.id.project_spinner);
                statusSpinner.setAdapter(adapter);
                AutoCompleteSpinnerAdapter.setUpdateListener(statusSpinner);
                Integer project_id = getCurrentProjectId();
                setProjectSelection(project_id);

                callbackParam.projectsLoaded();
            }
        });
    }

    protected void setProjectSelection(Integer projectId){
        if(projectId != null) {
            AutoCompleteSpinnerAdapter.setSelectedValue(findViewById(R.id.project_spinner),
                    String.valueOf(projectId));
        }
        else{
            AutoCompleteSpinnerAdapter.setSelectedValue(findViewById(R.id.project_spinner), "0");
        }
    }

    protected Integer getProjectId(){
        AutoCompleteTextView projectSpinner = findViewById(R.id.project_spinner);
        Integer proj_id = Integer.parseInt(AutoCompleteSpinnerAdapter
                .getSelectedSpinnerValue(projectSpinner));
        return (proj_id > 0) ? proj_id : null;
    }
}
