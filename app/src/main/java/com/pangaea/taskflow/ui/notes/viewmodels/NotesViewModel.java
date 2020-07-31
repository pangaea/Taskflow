package com.pangaea.taskflow.ui.notes.viewmodels;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.NoteRepository;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemsViewModel;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NotesViewModel extends ItemsViewModel {
    private NoteRepository repoNotes;
    private LiveData<List<Note>> mAllNotes;

    public NotesViewModel(Application application) {
        super(application);
        repoNotes = ((TaskflowApp) application).getNoteRepository();
        mAllNotes = repoNotes.getAllNotes();
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
}