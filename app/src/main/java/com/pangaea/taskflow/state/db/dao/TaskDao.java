package com.pangaea.taskflow.state.db.dao;

import com.pangaea.taskflow.state.db.entities.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAll();

    @Query("SELECT * FROM tasks WHERE id IN (:taskIds)")
    LiveData<List<Task>> loadAllByIds(int[] taskIds);

    @Query("SELECT * FROM tasks WHERE project_id IS NULL")
    LiveData<List<Task>> getGlobal();

    @Query("SELECT * FROM tasks WHERE project_id IN (:projectIds)")
    LiveData<List<Task>> getByProject(int[] projectIds);

    @Insert
    long[] insertAll(Task... tasks);

    @Insert
    long insert(Task note);

    @Update
    void update(Task note);

    @Delete
    void delete(Task note);

    @Query("DELETE FROM tasks")
    void deleteAll();
}