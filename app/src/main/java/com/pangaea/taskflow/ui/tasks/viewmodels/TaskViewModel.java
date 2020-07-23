package com.pangaea.taskflow.ui.tasks.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.TaskRepository;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TaskViewModel extends ViewModel implements ItemViewModel<Task> {
    ProjectRepository repoProjects;
    private LiveData<List<Project>> mAllProjects;
    TaskRepository repoTasks;
    LiveData<List<Task>> tasks;

    public TaskViewModel(Application application, int noteId) {
        repoTasks = ((TaskflowApp) application).getTaskRepository();
        tasks = repoTasks.getTasksById(noteId);

        repoProjects = ((TaskflowApp) application).getProjectRepository();
        mAllProjects = repoProjects.getAllProjects();
    }

    @Override public LiveData<List<Task>> getModel() {
        return tasks;
    }
    @Override public void insert(Task task) { repoTasks.insert(task); }
    @Override public void update(Task task) { repoTasks.update(task); }
    @Override public void delete(Task task) { repoTasks.delete(task); }
    public LiveData<List<Project>> getProjects() {
        return mAllProjects;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final int mTaskId;

        public Factory(@NonNull Application application, int taskId) {
            mApplication = application;
            mTaskId = taskId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TaskViewModel(mApplication, mTaskId);
        }
    }
}
