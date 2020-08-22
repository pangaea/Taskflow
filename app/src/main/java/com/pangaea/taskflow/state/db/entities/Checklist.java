package com.pangaea.taskflow.state.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
