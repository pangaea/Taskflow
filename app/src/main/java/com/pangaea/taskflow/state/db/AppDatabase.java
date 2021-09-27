package com.pangaea.taskflow.state.db;

import android.content.Context;
import android.os.AsyncTask;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.ChecklistRepository;
import com.pangaea.taskflow.state.NoteRepository;
import com.pangaea.taskflow.state.ProjectRepository;
import com.pangaea.taskflow.state.TaskRepository;
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
import java.util.List;

import androidx.annotation.NonNull;

import androidx.annotation.VisibleForTesting;
import androidx.room.*;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {
        Project.class,
        Note.class,
        Checklist.class,
        ChecklistItem.class,
        Task.class},
        version = 11,
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
                            //.fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .addMigrations(MIGRATION_10_11)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Room uses an own database hash to uniquely identify the database
            // Since version 1 does not use Room, it doesn't have the database hash associated.
            // By implementing a Migration class, we're telling Room that it should use the data
            // from version 10 to version 11.
            // If no migration is provided, then the tables will be dropped and recreated.
            database.execSQL("ALTER TABLE 'projects' ADD COLUMN 'created_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'projects' ADD COLUMN 'modified_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'tasks' ADD COLUMN 'created_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'tasks' ADD COLUMN 'modified_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'notes' ADD COLUMN 'created_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'notes' ADD COLUMN 'modified_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'checklists' ADD COLUMN 'created_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'checklists' ADD COLUMN 'modified_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'checklist_items' ADD COLUMN 'created_at' TEXT NOT NULL");
            database.execSQL("ALTER TABLE 'checklist_items' ADD COLUMN 'modified_at' TEXT NOT NULL");
        }
    };

    private static RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new PopulateDbAsync(appContext).execute();
            }
//            @Override
//            public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                super.onOpen(db);
//                new PopulateDbAsync(appContext).execute();
//            }
        };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private TaskflowApp mainApp;
        PopulateDbAsync(Context appContext) {
            mainApp = (TaskflowApp)appContext;
        }

        private String getStr(int id) {
            return appContext.getResources().getString(id);
        }

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                ProjectRepository repoProjects = mainApp.getProjectRepository();
                TaskRepository repoTasks = mainApp.getTaskRepository();
                NoteRepository repoNotes = mainApp.getNoteRepository();
                ChecklistRepository repoChecklists = mainApp.getChecklistRepository();

                int pjId = 1;
                Project p = new Project(getStr(R.string.sample_project_name), getStr(R.string.sample_project_description));
                p.id = pjId;
                repoProjects.insert(p);
                repoNotes.insert(new Note(getStr(R.string.sample_note_tips_title), getStr(R.string.sample_note_tips_content)));
                repoTasks.insert(new Task(getStr(R.string.sample_task_learn_name), getStr(R.string.sample_task_learn_details), TaskStatus.ACTIVE));
                repoTasks.insert(new Task(getStr(R.string.sample_project_task1_name), getStr(R.string.sample_project_task1_description), TaskStatus.INACTIVE, pjId));
                repoTasks.insert(new Task(getStr(R.string.sample_project_task2_name), getStr(R.string.sample_project_task2_description), TaskStatus.INACTIVE, pjId));

                int clId = 1;
                Checklist objList1 = new Checklist(getStr(R.string.sample_list_features_title),
                        getStr(R.string.sample_list_features_description));
                objList1.id = clId;
                List<ChecklistItem> items1 = new ArrayList<>();
                items1.add(new ChecklistItem(getStr(R.string.sample_list_features_item1), false, clId, 0));
                items1.add(new ChecklistItem(getStr(R.string.sample_list_features_item2), false, clId, 1));
                items1.add(new ChecklistItem(getStr(R.string.sample_list_features_item3), false, clId, 2));
                items1.add(new ChecklistItem(getStr(R.string.sample_list_features_item4), false, clId, 3));
                repoChecklists.insert(new ChecklistWithItems(objList1, items1));

                int clId2 = 2;
                Checklist objList2 = new Checklist(getStr(R.string.sample_project_list_title),
                        getStr(R.string.sample_project_list_description), pjId);
                objList2.id = clId2;
                List<ChecklistItem> items2 = new ArrayList<>();
                items2.add(new ChecklistItem(getStr(R.string.sample_project_list_item1), false, clId2, 0));
                items2.add(new ChecklistItem(getStr(R.string.sample_project_list_item2), false, clId2, 1));
                items2.add(new ChecklistItem(getStr(R.string.sample_project_list_item3), false, clId2, 2));
                repoChecklists.insert(new ChecklistWithItems(objList2, items2));
            }
            catch(Exception e){
                throw e;
            }

            return null;
        }
    }
}