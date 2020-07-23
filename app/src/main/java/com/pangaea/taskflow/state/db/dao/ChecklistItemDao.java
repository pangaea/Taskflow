package com.pangaea.taskflow.state.db.dao;

import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.ChecklistItem;
import com.pangaea.taskflow.state.db.entities.ChecklistWithItems;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ChecklistItemDao {
    @Query("SELECT * FROM checklist_items")
    LiveData<List<ChecklistItem>> getAll();

    @Query("SELECT * FROM checklist_items WHERE id IN (:checklistitemIds)")
    LiveData<List<ChecklistItem>> loadAllByIds(int[] checklistitemIds);

    @Insert
    void insert(ChecklistItem checklist);

    @Update
    void update(ChecklistItem checklist);

    @Delete
    void delete(ChecklistItem checklist);

    @Query("DELETE FROM checklist_items WHERE checklist_id=:id")
    void deleteAllByChecklist(int id);

    @Query("DELETE FROM checklist_items")
    void deleteAll();
}
