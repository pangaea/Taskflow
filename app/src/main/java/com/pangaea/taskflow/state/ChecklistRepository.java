package com.pangaea.taskflow.state;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.AppDatabase;
import com.pangaea.taskflow.state.db.dao.ChecklistDao;
import com.pangaea.taskflow.state.db.dao.ChecklistItemDao;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.ChecklistItem;
import com.pangaea.taskflow.state.db.entities.ChecklistWithItems;
import com.pangaea.taskflow.state.db.entities.Note;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class ChecklistRepository {
    AppDatabase db;
    private ChecklistDao mChecklistDao;
    private ChecklistItemDao mChecklistItemDao;

    public ChecklistRepository(Application application) {
        db = ((TaskflowApp) application).getDatabase();
        mChecklistDao = db.checklistDao();
        mChecklistItemDao = db.checklistItemDao();
    }

    public LiveData<List<Checklist>> getAllChecklists() {
        return mChecklistDao.getAll();
    }

    public LiveData<List<Checklist>> getChecklistsById(int id) {
        return mChecklistDao.loadAllByIds(new int[]{id});
    }

    public LiveData<List<ChecklistWithItems>> getChecklistsWithItemsById(int id) {
        return mChecklistDao.getChecklistWithItemsByIds(new int[]{id});
    }

    public LiveData<List<Checklist>> getGlobalChecklists() {
        return mChecklistDao.getGlobal();
    }

    public LiveData<List<Checklist>> getChecklistsByProject(int project_id) {
        return mChecklistDao.getByProject(new int[]{project_id});
    }

    public void insert (ChecklistWithItems checklist) {
        long curTime = System.currentTimeMillis();
        new ModelAsyncTask<ChecklistDao, ChecklistWithItems>(mChecklistDao, new ModelAsyncTask.ModelAsyncListener<ChecklistDao, ChecklistWithItems>(){
            @Override
            public void onExecute(ChecklistDao dao, ChecklistWithItems obj){
                obj.checklist.createdAt = new Date(curTime);
                obj.checklist.modifiedAt = new Date(curTime);
                int listId = (int)dao.insert(obj.checklist);
                if( obj.items != null ) {
                    insertChecklistItems(obj.items, listId);
                }
            }
        }).execute(checklist);
    }

    public void update (ChecklistWithItems checklist) {
        new ModelAsyncTask<ChecklistDao, ChecklistWithItems>(mChecklistDao, new ModelAsyncTask.ModelAsyncListener<ChecklistDao, ChecklistWithItems>(){
            @Override
            public void onExecute(ChecklistDao dao, ChecklistWithItems obj){
                obj.checklist.modifiedAt = new Date(System.currentTimeMillis());
                dao.update(obj.checklist);
                if( obj.items != null ) {
                    mChecklistItemDao.deleteAllByChecklist(obj.checklist.id);
                    insertChecklistItems(obj.items, -1);
                }
            }
        }).execute(checklist);
    }

    public void delete (ChecklistWithItems checklist) {
        new ModelAsyncTask<ChecklistDao, ChecklistWithItems>(mChecklistDao, new ModelAsyncTask.ModelAsyncListener<ChecklistDao, ChecklistWithItems>(){
            @Override
            public void onExecute(ChecklistDao dao, ChecklistWithItems obj){
                dao.delete(obj.checklist);
            }
        }).execute(checklist);
    }

    private void insertChecklistItems(List<ChecklistItem> items, int idNew){
        for (int i = 0, _size = items.size(); i < _size; i++) {
            ChecklistItem item = items.get(i);
            if( idNew > 0 ) item.checklist_id = idNew;
            long curTime = System.currentTimeMillis();
            item.createdAt = new Date(curTime);
            item.modifiedAt = new Date(curTime);
            mChecklistItemDao.insert(item);
        }
    }

}
