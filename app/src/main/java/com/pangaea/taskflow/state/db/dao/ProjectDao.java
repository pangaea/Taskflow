package com.pangaea.taskflow.state.db.dao;

import com.pangaea.taskflow.state.db.entities.Project;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProjectDao {
    @Query("SELECT * FROM projects")
    LiveData<List<Project>> getAll();

    @Query("SELECT * FROM projects WHERE id IN (:projectIds)")
    LiveData<List<Project>> loadAllByIds(int[] projectIds);

    @Insert
    long insert(Project project);

    @Update
    void update(Project project);

    @Delete
    void delete(Project project);

    @Query("DELETE FROM projects")
    void deleteAll();
}