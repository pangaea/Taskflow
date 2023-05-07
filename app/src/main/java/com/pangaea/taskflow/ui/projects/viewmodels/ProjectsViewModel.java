package com.pangaea.taskflow.ui.projects.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.NoteRepository;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;

import java.util.List;
import java.util.function.Consumer;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProjectsViewModel extends AndroidViewModel {
    ProjectRepository repoProjects;

    public ProjectsViewModel(Application application) {
        super(application);
        repoProjects = ((TaskflowApp) application).getProjectRepository();
    }

    public LiveData<List<Project>> getAllProjects() {
        return repoProjects.getAllProjects();
    }
    public void insert(Project project, Consumer<Long> callback) {
        repoProjects.insert(project, callback);
    }
}