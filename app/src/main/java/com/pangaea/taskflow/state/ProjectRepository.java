package com.pangaea.taskflow.state;

import android.app.Application;

import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.AppDatabase;
import com.pangaea.taskflow.state.db.dao.ProjectDao;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.converters.TimestampConverter;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import androidx.lifecycle.LiveData;

public class ProjectRepository extends EntityMetadata<Project> {
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
        insert(project, null);
    }

    public void insert (Project project, Consumer<Long> callback) {
        new ModelAsyncTask<ProjectDao, Project>(mProjectDao,
                new ModelAsyncTask.ModelAsyncListener<ProjectDao, Project>(){
            @Override
            public void onExecute(ProjectDao dao, Project obj){
                long id = dao.insert(obj);
                Optional.ofNullable(callback).ifPresent(o -> o.accept(id));
            }
        }).execute(insertWithTimestamp(project));
    }

    public void update (Project project) {
        new ModelAsyncTask<ProjectDao, Project>(mProjectDao,
                new ModelAsyncTask.ModelAsyncListener<ProjectDao, Project>(){
            @Override
            public void onExecute(ProjectDao dao, Project obj){
                //dao.update(obj);
                dao.updateData(obj.id, obj.name, obj.description,
                        TimestampConverter.dateToTimestamp(obj.modifiedAt));
            }
        }).execute(updateWithTimestamp(project));
    }

    public void delete (Project project) {
        new ModelAsyncTask<ProjectDao, Project>(mProjectDao,
                new ModelAsyncTask.ModelAsyncListener<ProjectDao, Project>(){
            @Override
            public void onExecute(ProjectDao dao, Project obj){
                dao.delete(obj);
            }
        }).execute(project);
    }

}
