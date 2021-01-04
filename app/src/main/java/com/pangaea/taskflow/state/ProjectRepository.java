package com.pangaea.taskflow.state;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.AppDatabase;
import com.pangaea.taskflow.state.db.dao.ProjectDao;
import com.pangaea.taskflow.state.db.entities.Project;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class ProjectRepository {
    private ProjectDao mProjectDao;

    public ProjectRepository(Application application) {
        AppDatabase db = ((TaskflowApp) application).getDatabase();
        mProjectDao = db.projectDao();
    }

    public LiveData<List<Project>> getAllProjects() {
        return mProjectDao.getAll();
    }

    public LiveData<List<Project>> getProjectsById(int id) {
        return mProjectDao.loadAllByIds(new int[]{id});
    }

    public void insert (Project project) {
        long curTime = System.currentTimeMillis();
        project.createdAt = new Date(curTime);
        project.modifiedAt = new Date(curTime);
        new ModelAsyncTask<ProjectDao, Project>(mProjectDao, new ModelAsyncTask.ModelAsyncListener<ProjectDao, Project>(){
            @Override
            public void onExecute(ProjectDao dao, Project obj){
                dao.insert(obj);
            }
        }).execute(project);
    }

    public void update (Project project) {
        project.modifiedAt = new Date(System.currentTimeMillis());
        new ModelAsyncTask<ProjectDao, Project>(mProjectDao, new ModelAsyncTask.ModelAsyncListener<ProjectDao, Project>(){
            @Override
            public void onExecute(ProjectDao dao, Project obj){
                dao.update(obj);
            }
        }).execute(project);
    }

    public void delete (Project project) {
        new ModelAsyncTask<ProjectDao, Project>(mProjectDao, new ModelAsyncTask.ModelAsyncListener<ProjectDao, Project>(){
            @Override
            public void onExecute(ProjectDao dao, Project obj){
                dao.delete(obj);
            }
        }).execute(project);
    }

}
