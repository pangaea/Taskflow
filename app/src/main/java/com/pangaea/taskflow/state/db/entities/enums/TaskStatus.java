package com.pangaea.taskflow.state.db.entities.enums;

import androidx.room.TypeConverter;

public enum TaskStatus {
    ACTIVE(0),
    INACTIVE(1),
    COMPLETED(2);

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

}