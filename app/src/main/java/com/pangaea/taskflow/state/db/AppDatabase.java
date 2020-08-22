package com.pangaea.taskflow.state.db;

import android.content.Context;
import android.os.AsyncTask;

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
        version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE = null;

    public abstract ProjectDao projectDao();
    public abstract NoteDao noteDao();
    public abstract TaskDao taskDao();
    public abstract ChecklistDao checklistDao();
    public abstract ChecklistItemDao checklistItemDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "taskflow")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            //.addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
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
                        new Project("basement gym", "Clearing out some space for a gym in the basement")
                );
                mDao.insertAll(
                        new Note("todo list", "My todo list content"),
                        new Note("scratch pad", "Misc stuff..."),
                        new Note("basement ideas", "Ideas go here", pjId)
                );
                taskDao.insertAll(
                        new Task("Clean garage", "Get garage ready for winter", TaskStatus.ACTIVE),
                        new Task("Trim Bushes", "Need to prune the bushes in the backyard", TaskStatus.INACTIVE),
                        new Task("Clear Clutter", "Throw out all clutter", TaskStatus.INACTIVE, pjId)
                );

                int clId = (int)checklistDao.insert(new Checklist("Shopping list", "List I need from the store."));
                checklistItemDao.insert( new ChecklistItem("Extension cord", false, clId, 0));
                checklistItemDao.insert( new ChecklistItem("Light bulbs", false, clId, 1));
                checklistItemDao.insert( new ChecklistItem("Hammer drill", false, clId, 2));

                int clId2 = (int)checklistDao.insert(new Checklist("Stuff to throw out", "List I need to throw out.", pjId));
                checklistItemDao.insert( new ChecklistItem("Books", false, clId2, 0));
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