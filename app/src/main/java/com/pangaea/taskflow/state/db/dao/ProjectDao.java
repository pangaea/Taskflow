package com.pangaea.taskflow.state.db.dao;

import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProjectDao {
    @Query("SELECT * FROM projects order by modified_at desc")
    LiveData<List<Project>> getAll();

    @Query("SELECT * FROM projects WHERE id IN (:projectIds) order by modified_at desc")
    LiveData<List<Project>> loadAllByIds(int[] projectIds);

    @Insert
    long insert(Project project);

    @Update
    void update(Project project);

    @Query("UPDATE projects SET name = :name, description = :description, modified_at = :modified_at WHERE id = :id")
    int updateData(long id, String name, String description, String modified_at);

    @Delete
    void delete(Project project);

    @Query("DELETE FROM projects")
    void deleteAll();
}