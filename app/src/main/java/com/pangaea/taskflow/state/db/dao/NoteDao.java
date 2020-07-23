package com.pangaea.taskflow.state.db.dao;

import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.*;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getAll();

    @Query("SELECT * FROM notes WHERE id IN (:noteIds)")
    LiveData<List<Note>> loadAllByIds(int[] noteIds);

    @Query("SELECT * FROM notes WHERE project_id IS NULL")
    LiveData<List<Note>> getGlobal();

    @Query("SELECT * FROM notes WHERE project_id IN (:projectIds)")
    LiveData<List<Note>> getByProject(int[] projectIds);

    @Insert
    long[] insertAll(Note... notes);

    @Insert
    long insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM notes")
    void deleteAll();
}