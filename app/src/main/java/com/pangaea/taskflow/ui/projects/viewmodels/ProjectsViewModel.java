package com.pangaea.taskflow.ui.projects.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.NoteRepository;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProjectsViewModel extends AndroidViewModel {
    ProjectRepository repoProjects;
    private LiveData<List<Project>> mAllProjects;

    public ProjectsViewModel(Application application) {
        super(application);
        repoProjects = ((TaskflowApp) application).getProjectRepository();
        mAllProjects = repoProjects.getAllProjects();
    }

    public LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }
}