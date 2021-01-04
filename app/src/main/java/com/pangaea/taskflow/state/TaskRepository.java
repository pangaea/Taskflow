package com.pangaea.taskflow.state;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.AppDatabase;
import com.pangaea.taskflow.state.db.dao.TaskDao;
import com.pangaea.taskflow.state.db.entities.Task;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TaskRepository extends EntityMetadata<Task> {
    private TaskDao mTaskDao;

    public TaskRepository(Application application) {
        AppDatabase db = ((TaskflowApp) application).getDatabase();
        mTaskDao = db.taskDao();
    }

    public LiveData<List<Task>> getAllTasks() {
        return mTaskDao.getAll();
    }

    public LiveData<List<Task>> getTasksById(int id) {
        return mTaskDao.loadAllByIds(new int[]{id});
    }

    public LiveData<List<Task>> getGlobalTasks() {
        return mTaskDao.getGlobal();
    }

    public LiveData<List<Task>> getTasksByProject(int project_id) {
        return mTaskDao.getByProject(new int[]{project_id});
    }

    public void insert (Task task) {
        new ModelAsyncTask<TaskDao, Task>(mTaskDao, new ModelAsyncTask.ModelAsyncListener<TaskDao, Task>(){
            @Override
            public void onExecute(TaskDao dao, Task obj){
                dao.insert(obj);
            }
        }).execute(insertWithTimestamp(task));
    }

    public void update (Task task) {
        new ModelAsyncTask<TaskDao, Task>(mTaskDao, new ModelAsyncTask.ModelAsyncListener<TaskDao, Task>(){
            @Override
            public void onExecute(TaskDao dao, Task obj){
                dao.update(obj);
            }
        }).execute(updateWithTimestamp(task));
    }

    public void delete (Task task) {
        new ModelAsyncTask<TaskDao, Task>(mTaskDao, new ModelAsyncTask.ModelAsyncListener<TaskDao, Task>(){
            @Override
            public void onExecute(TaskDao dao, Task obj){
                dao.delete(obj);
            }
        }).execute(task);
    }
}
