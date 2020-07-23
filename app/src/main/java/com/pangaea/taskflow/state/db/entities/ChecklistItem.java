package com.pangaea.taskflow.state.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "checklist_items",
        foreignKeys = @ForeignKey(entity = Checklist.class,
                parentColumns = "id",
                childColumns = "checklist_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("checklist_id")})
public class ChecklistItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "checklist_id")
    @NonNull
    public int checklist_id;

    @ColumnInfo(name = "position")
    public int position;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "checked")
    public Boolean checked;

    public ChecklistItem(@NonNull String name, boolean checked, @NonNull int checklist_id, int position ) {
        this.name = name;
        this.checked = checked;
        this.checklist_id = checklist_id;
        this.position = position;
    }
}
