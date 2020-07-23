package com.pangaea.taskflow.ui.projects.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.TaskRepository;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;
import com.pangaea.taskflow.ui.tasks.viewmodels.TaskViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProjectViewModel extends ViewModel implements ItemViewModel<Project> {
    ProjectRepository repoProjects;
    LiveData<List<Project>> projects;

    public ProjectViewModel(Application application, int projectId) {
        repoProjects = ((TaskflowApp) application).getProjectRepository();
        projects = repoProjects.getProjectsById(projectId);
    }

    @Override public LiveData<List<Project>> getModel() {
        return projects;
    }
    @Override public void insert(Project project) { repoProjects.insert(project); }
    @Override public void update(Project project) { repoProjects.update(project); }
    @Override public void delete(Project project) { repoProjects.delete(project); }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final int mProjectId;

        public Factory(@NonNull Application application, int projectId) {
            mApplication = application;
            mProjectId = projectId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProjectViewModel(mApplication, mProjectId);
        }
    }
}
