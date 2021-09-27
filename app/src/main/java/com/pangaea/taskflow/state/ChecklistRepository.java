package com.pangaea.taskflow.state;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.AppDatabase;
import com.pangaea.taskflow.state.db.dao.ChecklistDao;
import com.pangaea.taskflow.state.db.dao.ChecklistItemDao;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.ChecklistItem;
import com.pangaea.taskflow.state.db.entities.ChecklistWithItems;
import com.pangaea.taskflow.state.db.entities.converters.TimestampConverter;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ChecklistRepository extends EntityMetadata<Checklist> {
    AppDatabase db;
    private ChecklistDao mChecklistDao;
    private ChecklistItemDao mChecklistItemDao;
    private EntityMetadata mdItem = new EntityMetadata<ChecklistItem>();

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
        new ModelAsyncTask<ChecklistDao, ChecklistWithItems>(mChecklistDao, new ModelAsyncTask.ModelAsyncListener<ChecklistDao, ChecklistWithItems>(){
            @Override
            public void onExecute(ChecklistDao dao, ChecklistWithItems obj){
                int listId = (int)dao.insert(insertWithTimestamp(obj.checklist));
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
                //dao.update(updateWithTimestamp(obj.checklist));
                dao.updateData(obj.checklist.id, obj.checklist.name, obj.checklist.description,
                        TimestampConverter.dateToTimestamp(obj.checklist.modifiedAt));
                if( obj.items != null ) {
                    mChecklistItemDao.deleteAllByChecklist(obj.checklist.id);
                    insertChecklistItems(obj.items, -1);
                }
            }
        }).execute(updateChecklistDate(checklist));
    }

    private ChecklistWithItems updateChecklistDate(ChecklistWithItems checklist) {
        updateWithTimestamp(checklist.checklist);
        return checklist;
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
            mChecklistItemDao.insert((ChecklistItem)mdItem.insertWithTimestamp(item));
        }
    }

}
