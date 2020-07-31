package com.pangaea.taskflow.ui.shared.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.db.entities.Project;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ItemsViewModel extends AndroidViewModel {
    private ProjectRepository repoProjects;
    public ItemsViewModel(Application application) {
        super(application);
        repoProjects = ((TaskflowApp) application).getProjectRepository();
    }
    public LiveData<List<Project>> getProject(int i) {
        return repoProjects.getProjectsById(i);
    }
}
