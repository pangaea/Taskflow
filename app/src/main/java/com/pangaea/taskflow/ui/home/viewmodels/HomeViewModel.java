package com.pangaea.taskflow.ui.home.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.ChecklistRepository;
import com.pangaea.taskflow.state.NoteRepository;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.TaskRepository;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HomeViewModel extends AndroidViewModel {
    ProjectRepository repoProject;
    NoteRepository repoNotes;
    TaskRepository repoTasks;
    ChecklistRepository repoChecklists;
    private LiveData<List<Note>> mAllNotes;
    private LiveData<List<Task>> mAllTasks;
    private LiveData<List<Checklist>> mAllChecklists;
    private LiveData<List<Project>> mAllProjects;

    public HomeViewModel(Application application) {
        super(application);
        repoProject = ((TaskflowApp) application).getProjectRepository();

        repoNotes = ((TaskflowApp) application).getNoteRepository();
        mAllNotes = repoNotes.getAllNotes();

        repoTasks = ((TaskflowApp) application).getTaskRepository();
        mAllTasks = repoTasks.getAllTasks();

        repoChecklists = ((TaskflowApp) application).getChecklistRepository();
        mAllChecklists = repoChecklists.getAllChecklists();

        mAllProjects = repoProject.getAllProjects();
    }

    public LiveData<List<Project>> getProject(int i) {
        return repoProject.getProjectsById(i);
    }
    public LiveData<List<Project>> getProjects() {
        return mAllProjects;
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }
    public LiveData<List<Note>> getGlobalNotes() {
        return repoNotes.getGlobalNotes();
    }
    public LiveData<List<Note>> getNotesByProject(int i) {
        return repoNotes.getNotesByProject(i);
    }

    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }
    public LiveData<List<Task>> getGlobalTasks() {
        return repoTasks.getGlobalTasks();
    }
    public LiveData<List<Task>> getTasksByProject(int i) {
        return repoTasks.getTasksByProject(i);
    }

    public LiveData<List<Checklist>> getAllChecklists() {
        return mAllChecklists;
    }
    public LiveData<List<Checklist>> getGlobalChecklists() {
        return repoChecklists.getGlobalChecklists();
    }
    public LiveData<List<Checklist>> getChecklistsByProject(int i) {
        return repoChecklists.getChecklistsByProject(i);
    }
}