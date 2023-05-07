package com.pangaea.taskflow.ui.checklists.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.ChecklistRepository;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.db.entities.ChecklistWithItems;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;

import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChecklistViewModel extends ViewModel implements ItemViewModel<ChecklistWithItems> {
    ProjectRepository repoProjects;
    private LiveData<List<Project>> mAllProjects;
    ChecklistRepository repoChecklists;
    LiveData<List<ChecklistWithItems>> checklists;

    public ChecklistViewModel(Application application, int noteId) {
        repoChecklists = ((TaskflowApp) application).getChecklistRepository();
        checklists = repoChecklists.getChecklistsWithItemsById(noteId);

        repoProjects = ((TaskflowApp) application).getProjectRepository();
        mAllProjects = repoProjects.getAllProjects();
    }

    @Override public LiveData<List<ChecklistWithItems>> getModel() {
        return checklists;
    }
    @Override public void update(ChecklistWithItems checklist) { repoChecklists.update(checklist); }
    @Override public void delete(ChecklistWithItems checklist) { repoChecklists.delete(checklist); }
    public LiveData<List<Project>> getProjects() {
        return mAllProjects;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final int mChecklistId;

        public Factory(@NonNull Application application, int checklistId) {
            mApplication = application;
            mChecklistId = checklistId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ChecklistViewModel(mApplication, mChecklistId);
        }
    }
}
