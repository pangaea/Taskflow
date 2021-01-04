package com.pangaea.taskflow.state.db.entities;

import com.pangaea.taskflow.state.db.entities.converters.TimestampConverter;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "projects")
public class Project {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    public Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    public Date modifiedAt;

    public Project(@NonNull String name, String description) {
        this.name = name;
        this.description = description;
    }
}
