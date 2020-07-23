package com.pangaea.taskflow.state;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.AppDatabase;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.dao.NoteDao;
import com.pangaea.taskflow.state.db.entities.Task;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NoteRepository {
    private NoteDao mNoteDao;
    //private LiveData<List<Note>> mAllNotes;

    public NoteRepository(Application application) {
        //AppDatabase db = AppDatabase.getDatabase(application);
        AppDatabase db = ((TaskflowApp) application).getDatabase();
        mNoteDao = db.noteDao();
        //mAllNotes = mNoteDao.getAll();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mNoteDao.getAll();
    }

    public LiveData<List<Note>> getNotesById(int id) {
        return mNoteDao.loadAllByIds(new int[]{id});
    }

    public LiveData<List<Note>> getGlobalNotes() {
        return mNoteDao.getGlobal();
    }

    public LiveData<List<Note>> getNotesByProject(int project_id) {
        return mNoteDao.getByProject(new int[]{project_id});
    }

    public void insert (Note note) {
        new ModelAsyncTask<NoteDao, Note>(mNoteDao, new ModelAsyncTask.ModelAsyncListener<NoteDao, Note>(){
            @Override
            public void onExecute(NoteDao dao, Note obj){
                dao.insert(obj);
            }
        }).execute(note);
    }

    public void update (Note note) {
        new ModelAsyncTask<NoteDao, Note>(mNoteDao, new ModelAsyncTask.ModelAsyncListener<NoteDao, Note>(){
            @Override
            public void onExecute(NoteDao dao, Note obj){
                dao.update(obj);
            }
        }).execute(note);
    }

    public void delete (Note note) {
        new ModelAsyncTask<NoteDao, Note>(mNoteDao, new ModelAsyncTask.ModelAsyncListener<NoteDao, Note>(){
            @Override
            public void onExecute(NoteDao dao, Note obj){
                dao.delete(obj);
            }
        }).execute(note);
    }

}
