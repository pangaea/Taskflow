package com.pangaea.taskflow.ui.checklists.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.ChecklistRepository;
import com.pangaea.taskflow.state.NoteRepository;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemsViewModel;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChecklistsViewModel extends ItemsViewModel {
    private ChecklistRepository repoChecklists;
    private LiveData<List<Checklist>> mAllChecklists;

    public ChecklistsViewModel(Application application) {
        super(application);
        repoChecklists = ((TaskflowApp) application).getChecklistRepository();
        mAllChecklists = repoChecklists.getAllChecklists();
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