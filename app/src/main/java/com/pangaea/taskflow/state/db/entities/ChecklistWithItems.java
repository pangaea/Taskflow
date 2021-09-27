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

        if (this.items != null) {
            // Set created and updated timestamps to match checklist
            for (int i = 0; i < this.items.size(); i++) {
                this.items.get(i).createdAt = checklist.createdAt;
                this.items.get(i).modifiedAt = checklist.modifiedAt;
            }
        }
    }
}
