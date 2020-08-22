package com.pangaea.taskflow.state.db.entities;

import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "tasks",
        foreignKeys = @ForeignKey(entity = Project.class,
                parentColumns = "id",
                childColumns = "project_id"),
        indices = {@Index("project_id")})
@TypeConverters({TaskStatus.class})
public class Task {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "project_id")
    public Integer project_id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "details")
    public String details;

    @ColumnInfo(name = "status")
    public TaskStatus status;

    public Task(@NonNull String name, String details, @NonNull TaskStatus status) {
        this.name = name;
        this.details = details;
        this.status = status;
    }

    @Ignore
    public Task(@NonNull String name, String details, @NonNull TaskStatus status, @NonNull Integer project_id) {
        this.name = name;
        this.details = details;
        this.status = status;
        this.project_id = project_id;
    }
}
