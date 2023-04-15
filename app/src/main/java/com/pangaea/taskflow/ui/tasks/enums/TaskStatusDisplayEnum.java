package com.pangaea.taskflow.ui.tasks.enums;

import android.content.Context;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public enum TaskStatusDisplayEnum {
    TODO(TaskStatus.TODO),
    STARTED(TaskStatus.STARTED),
    COMPLETE(TaskStatus.COMPLETE),
    PAUSED(TaskStatus.PAUSED);

    TaskStatus status;
    TaskStatusDisplayEnum(TaskStatus status) {
        this.status = status;
    }

    public String displayText(Context ctx) {
        int resId = ctx.getResources().getIdentifier("TaskStatus_" + status.name(),
                "string", ctx.getPackageName());
        return ctx.getResources().getString(resId);
    }

    public static List<Pair<String, String>> spinnerData(Context ctx) {
        List<Pair<String, String>> statuses = new ArrayList<Pair<String, String>>();
        for (TaskStatusDisplayEnum elem : TaskStatusDisplayEnum.values()) {
            statuses.add(new Pair<String, String>(elem.name(), elem.displayText(ctx)));
        }
        return statuses;
    }
}
