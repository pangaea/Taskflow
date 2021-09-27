package com.pangaea.taskflow.state.db.dao;

import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks order by modified_at desc")
    LiveData<List<Task>> getAll();

    @Query("SELECT * FROM tasks WHERE id IN (:taskIds) order by modified_at desc")
    LiveData<List<Task>> loadAllByIds(int[] taskIds);

    @Query("SELECT * FROM tasks WHERE project_id IS NULL order by modified_at desc")
    LiveData<List<Task>> getGlobal();

    @Query("SELECT * FROM tasks WHERE project_id IN (:projectIds) order by modified_at desc")
    LiveData<List<Task>> getByProject(int[] projectIds);

    @Insert
    long[] insertAll(Task... tasks);

    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Query("UPDATE tasks SET name = :name, details = :details, status = :status, project_id = :project_id, modified_at = :modified_at WHERE id = :id")
    int updateData(long id, String name, String details, int status, Integer project_id, String modified_at);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks")
    void deleteAll();
}