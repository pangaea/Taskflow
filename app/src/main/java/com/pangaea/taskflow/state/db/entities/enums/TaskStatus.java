package com.pangaea.taskflow.state.db.entities.enums;

import androidx.room.TypeConverter;

public enum TaskStatus {
    TODO(0),
    STARTED(1),
    COMPLETE(2),
    PAUSED(3);

    private final Integer code;

    TaskStatus(Integer value) {
        this.code = value;
    }
    public Integer getCode() {
        return code;
    }

    @TypeConverter
    public static TaskStatus getTaskStatus(Integer numeral){
        for(TaskStatus ds : values()){
            if(ds.code == numeral){
                return ds;
            }
        }
        return null;
    }

    @TypeConverter
    public static Integer getTaskStatusInt(TaskStatus status){
        if(status != null)
            return status.code;
        return null;
    }

    public static TaskStatus lookup(String name) {
        try {
            return TaskStatus.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}