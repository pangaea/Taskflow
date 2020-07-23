package com.pangaea.taskflow.ui.notes.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.NoteRepository;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NoteViewModel extends ViewModel implements ItemViewModel<Note> {
    ProjectRepository repoProjects;
    private LiveData<List<Project>> mAllProjects;
    NoteRepository repoNotes;
    LiveData<List<Note>>  notes;

    public NoteViewModel(Application application, int noteId) {
        repoNotes = ((TaskflowApp) application).getNoteRepository();
        notes = repoNotes.getNotesById(noteId);

        repoProjects = ((TaskflowApp) application).getProjectRepository();
        mAllProjects = repoProjects.getAllProjects();
    }

    @Override public LiveData<List<Note>> getModel() {
        return notes;
    }
    @Override public void insert(Note note) { repoNotes.insert(note); }
    @Override public void update(Note note) { repoNotes.update(note); }
    @Override public void delete(Note note) { repoNotes.delete(note); }
    public LiveData<List<Project>> getProjects() {
        return mAllProjects;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final int mNoteId;

        public Factory(@NonNull Application application, int noteId) {
            mApplication = application;
            mNoteId = noteId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new NoteViewModel(mApplication, mNoteId);
        }
    }
}