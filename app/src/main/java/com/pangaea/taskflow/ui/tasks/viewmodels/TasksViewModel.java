package com.pangaea.taskflow.ui.tasks.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.TaskRepository;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemsViewModel;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TasksViewModel extends ItemsViewModel {
    private TaskRepository repoTasks;
    private LiveData<List<Task>> mAllTasks;

    public TasksViewModel(Application application) {
        super(application);
        repoTasks = ((TaskflowApp) application).getTaskRepository();
        mAllTasks = repoTasks.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }
    public LiveData<List<Task>> getGlobalTasks(String sortBy) {
        return repoTasks.getGlobalTasks(sortBy);
    }
    public LiveData<List<Task>> getTasksByProject(int i, String sortBy) {
        return repoTasks.getTasksByProject(i, sortBy);
    }
}