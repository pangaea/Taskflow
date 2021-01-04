package com.pangaea.taskflow.state.db.entities;

import com.pangaea.taskflow.state.db.entities.converters.TimestampConverter;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(entity = Project.class,
                parentColumns = "id",
                childColumns = "project_id"),
        indices = {@Index("project_id")})
public class Note extends BaseEntity {
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

    @Ignore
    public Note(@NonNull String title, @NonNull String content, @NonNull Integer project_id) {
        this.title = title;
        this.content = content;
        this.project_id = project_id;
    }
}