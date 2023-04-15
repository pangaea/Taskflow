package com.pangaea.taskflow.state.db.dao;

import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.ChecklistWithItems;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface ChecklistDao {
    @Query("SELECT * FROM checklists order by modified_at desc")
    LiveData<List<Checklist>> getAll();

    @Query("SELECT * FROM checklists WHERE id IN (:checklistIds) order by modified_at desc")
    LiveData<List<Checklist>> loadAllByIds(int[] checklistIds);

    @Transaction
    @Query("SELECT * FROM checklists WHERE id IN (:checklistIds) order by modified_at desc")
    LiveData<List<ChecklistWithItems>> getChecklistWithItemsByIds(int[] checklistIds);

    @Query("SELECT * FROM checklists WHERE project_id IS NULL order by modified_at desc")
    LiveData<List<Checklist>> getGlobal();

    @Query("SELECT * FROM checklists WHERE project_id IN (:projectIds) order by modified_at desc")
    LiveData<List<Checklist>> getByProject(int[] projectIds);

    @Insert
    long insert(Checklist checklist);

    @Update
    void update(Checklist checklist);

    @Query("UPDATE checklists SET name = :name, description = :description, modified_at = :modified_at WHERE id = :id")
    int updateData(long id, String name, String description, String modified_at);

    @Delete
    void delete(Checklist checklist);

    @Query("DELETE FROM checklists")
    void deleteAll();
}