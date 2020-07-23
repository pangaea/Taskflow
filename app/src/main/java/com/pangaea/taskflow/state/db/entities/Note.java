package com.pangaea.taskflow.state.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(entity = Project.class,
                parentColumns = "id",
                childColumns = "project_id"),
        indices = {@Index("project_id")})
public class Note {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "project_id")
    public Integer project_id;

    @ColumnInfo(name = "title")
    @NonNull
    public String title;

    @ColumnInfo(name = "content")
    @NonNull
    public String content;

    public Note(@NonNull String title, @NonNull String content) {
        this.title = title;
        this.content = content;
    }
}