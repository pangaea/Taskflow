package com.pangaea.taskflow.state.db.entities;

import com.pangaea.taskflow.state.db.entities.converters.TimestampConverter;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "checklists",
        foreignKeys = @ForeignKey(entity = Project.class,
                parentColumns = "id",
                childColumns = "project_id"),
        indices = {@Index("project_id")})
public class Checklist {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "project_id")
    public Integer project_id;

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

    public Checklist(@NonNull String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Ignore
    public Checklist(@NonNull String name, String description, @NonNull Integer project_id) {
        this.name = name;
        this.description = description;
        this.project_id = project_id;
    }
}
