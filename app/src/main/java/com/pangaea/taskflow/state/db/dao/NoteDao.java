package com.pangaea.taskflow.state.db.dao;

import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Task;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.*;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes order by modified_at desc")
    LiveData<List<Note>> getAll();

    @Query("SELECT * FROM notes WHERE id IN (:noteIds) order by modified_at desc")
    LiveData<List<Note>> loadAllByIds(int[] noteIds);

    @Query("SELECT * FROM notes WHERE project_id IS NULL ")
    LiveData<List<Note>> getGlobal();

    @Query("SELECT * FROM notes  WHERE project_id IN (:projectIds)")
    LiveData<List<Note>> getByProject(int[] projectIds);

    @Insert
    long[] insertAll(Note... notes);

    @Insert
    long insert(Note note);

    @Update
    void update(Note note);

    @Query("UPDATE notes SET title = :title, content = :content, project_id = :project_id, modified_at = :modified_at WHERE id = :id")
    int updateData(long id, String title, String content, Integer project_id, String modified_at);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM notes")
    void deleteAll();
}