package com.pangaea.taskflow.state.db.entities;

import com.pangaea.taskflow.state.db.entities.converters.TimestampConverter;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

public class BaseEntity {
    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    @NotNull
    public Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    @NotNull
    public Date modifiedAt;
}
