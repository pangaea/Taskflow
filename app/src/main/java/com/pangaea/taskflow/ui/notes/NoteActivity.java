package com.pangaea.taskflow.ui.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.notes.viewmodels.NoteViewModel;
import com.pangaea.taskflow.ui.notes.viewmodels.NotesViewModel;
import com.pangaea.taskflow.ui.projects.viewmodels.ProjectViewModel;
import com.pangaea.taskflow.ui.shared.ItemActivity;
import com.pangaea.taskflow.ui.shared.ProjectAssociatedItemActivity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class NoteActivity extends ProjectAssociatedItemActivity<Note, NoteViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Extract note id
        final int noteId = getItemId();

        NoteViewModel.Factory factory = new NoteViewModel.Factory(
                getApplication(), noteId);

        final NoteViewModel model = new ViewModelProvider(this, factory)
                .get(NoteViewModel.class);

        // Project assign ///////////////////////////////////////////
        populateProjects(model.getProjects(), new ProjectsLoadedCallback(){
            @Override public void projectsLoaded(){
                subscribeToModel(model);
            }
        });

        if(noteId == -1)
            attachDirtyEvents(R.id.editTitle, R.id.editContent, R.id.project_spinner);
    }

    @Override public void fillFields(Note note){
        TextView tvTitle = findViewById(R.id.editTitle);
        tvTitle.setText(note.title);
        TextView tvContent = findViewById(R.id.editContent);
        tvContent.setText(note.content);

        // Project assign ///////////////////////////////////////////
        setProjectSelection(note.project_id);

        attachDirtyEvents(R.id.editTitle, R.id.editContent, R.id.project_spinner);
    }
    @Override public Note buildModel(){
        int noteId = getItemId();
        TextView tvTitle = findViewById(R.id.editTitle);
        TextView tvContent = findViewById(R.id.editContent);
        Note note = new Note(tvTitle.getText().toString(), tvContent.getText().toString());
        if(noteId > 0) note.id = noteId;
        // Project assign ///////////////////////////////////////////
        note.project_id = getProjectId();
        return note;
    }
    @Override public String deleteWarning(){
        TextView tvTitle = findViewById(R.id.editTitle);
        return "Are you sure you want to delete the note '" + tvTitle.getText().toString() + "'?";
    }
}
