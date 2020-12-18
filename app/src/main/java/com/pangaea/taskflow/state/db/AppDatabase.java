package com.pangaea.taskflow.state.db;

import android.content.Context;
import android.os.AsyncTask;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.dao.ChecklistDao;
import com.pangaea.taskflow.state.db.dao.ChecklistItemDao;
import com.pangaea.taskflow.state.db.dao.ProjectDao;
import com.pangaea.taskflow.state.db.dao.TaskDao;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.ChecklistItem;
import com.pangaea.taskflow.state.db.entities.ChecklistWithItems;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.dao.NoteDao;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {
        Project.class,
        Note.class,
        Checklist.class,
        ChecklistItem.class,
        Task.class},
        version = 10,
        exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE = null;
    private static Context appContext = null;

    public abstract ProjectDao projectDao();
    public abstract NoteDao noteDao();
    public abstract TaskDao taskDao();
    public abstract ChecklistDao checklistDao();
    public abstract ChecklistItemDao checklistItemDao();

    public static AppDatabase getDatabase(final Context context) {
        appContext = context;
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "taskflow")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final NoteDao mDao;
        private final ProjectDao projectDao;
        private final TaskDao taskDao;
        private final ChecklistDao checklistDao;
        private final ChecklistItemDao checklistItemDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.noteDao();
            projectDao = db.projectDao();
            taskDao = db.taskDao();
            checklistDao = db.checklistDao();
            checklistItemDao = db.checklistItemDao();
        }

        private String getStr(int id) {
            return appContext.getResources().getString(id);
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();
            taskDao.deleteAll();
            projectDao.deleteAll();
            checklistDao.deleteAll();
            checklistItemDao.deleteAll();

            try {
                final int pjId = (int)projectDao.insert(
                        new Project(getStr(R.string.sample_project_name), getStr(R.string.sample_project_description))
                );
                mDao.insertAll(
                        new Note(getStr(R.string.sample_note_tips_title), getStr(R.string.sample_note_tips_content))
                );
                taskDao.insertAll(
                        new Task(getStr(R.string.sample_task_learn_name), getStr(R.string.sample_task_learn_details), TaskStatus.ACTIVE),
                        new Task(getStr(R.string.sample_project_task1_name), getStr(R.string.sample_project_task1_description), TaskStatus.INACTIVE, pjId),
                        new Task(getStr(R.string.sample_project_task2_name), getStr(R.string.sample_project_task2_description), TaskStatus.INACTIVE, pjId)
                );

                int clId = (int)checklistDao.insert(new Checklist(appContext.getResources().getString(R.string.sample_list_features_title),
                        getStr(R.string.sample_list_features_description)));
                checklistItemDao.insert( new ChecklistItem(getStr(R.string.sample_list_features_item1), false, clId, 0));
                checklistItemDao.insert( new ChecklistItem(getStr(R.string.sample_list_features_item2), false, clId, 1));
                checklistItemDao.insert( new ChecklistItem(getStr(R.string.sample_list_features_item3), false, clId, 2));
                checklistItemDao.insert( new ChecklistItem(getStr(R.string.sample_list_features_item4), false, clId, 3));

                int clId2 = (int)checklistDao.insert(new Checklist(getStr(R.string.sample_project_list_title), getStr(R.string.sample_project_list_description), pjId));
                checklistItemDao.insert( new ChecklistItem(getStr(R.string.sample_project_list_item1), false, clId2, 0));
                checklistItemDao.insert( new ChecklistItem(getStr(R.string.sample_project_list_item2), false, clId2, 1));
                checklistItemDao.insert( new ChecklistItem(getStr(R.string.sample_project_list_item3), false, clId2, 2));
            }
            catch(Exception e){
                throw e;
            }

            // Test
            //List<ChecklistWithItems> lists = checklistDao.getChecklistWithItems();

            return null;
        }
    }
}