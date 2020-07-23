package com.pangaea.taskflow.state.db.entities;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

public class ChecklistWithItems {
    @Embedded
    public Checklist checklist;
    @Relation(
            parentColumn = "id",
            entityColumn = "checklist_id"
    )
    public List<ChecklistItem> items;

    public ChecklistWithItems(@NonNull Checklist checklist, List<ChecklistItem> items) {
        this.checklist = checklist;
        this.items = items;
    }
}
