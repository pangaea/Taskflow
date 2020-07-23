package com.pangaea.taskflow;

import android.app.Application;

import com.pangaea.taskflow.state.ChecklistRepository;
import com.pangaea.taskflow.state.NoteRepository;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.TaskRepository;
import com.pangaea.taskflow.state.db.AppDatabase;

/**
 * Android Application class. Used for accessing singletons.
 */
public class TaskflowApp extends Application {

    //private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        //mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getDatabase(this);
    }

    public NoteRepository getNoteRepository() {
        return new NoteRepository(this);
    }
    public TaskRepository getTaskRepository() {
        return new TaskRepository(this);
    }
    public ProjectRepository getProjectRepository() {
        return new ProjectRepository(this);
    }
    public ChecklistRepository getChecklistRepository() {
        return new ChecklistRepository(this);
    }
}