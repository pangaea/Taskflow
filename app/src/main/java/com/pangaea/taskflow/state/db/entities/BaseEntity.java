package com.pangaea.taskflow.state.db.entities;

import com.pangaea.taskflow.state.db.entities.converters.TimestampConverter;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

public class BaseEntity {
    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    public Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    public Date modifiedAt;
}
