package com.pangaea.taskflow.state;

import com.pangaea.taskflow.state.db.entities.BaseEntity;

import java.util.Date;

public class EntityMetadata<T extends BaseEntity> {
    public T insertWithTimestamp(T o) {
        long curTime = System.currentTimeMillis();
        o.createdAt = new Date(curTime);
        o.modifiedAt = new Date(curTime);
        return o;
    }
    public T updateWithTimestamp(T o) {
        long curTime = System.currentTimeMillis();
        o.modifiedAt = new Date(curTime);
        return o;
    }
}
