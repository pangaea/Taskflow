package com.pangaea.taskflow.ui.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import jp.wasabeef.richeditor.RichEditor;

public class NoteActivity extends ProjectAssociatedItemActivity<Note, NoteViewModel> {

    private RichEditor mEditor;
    private TextView mContent;

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

        mEditor = findViewById(R.id.editContent2);
        mEditor.setEditorHeight(200);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Details...");

        mContent = findViewById(R.id.editContent);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                mContent.setText(text);
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

//        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setSubscript();
//            }
//        });
//
//        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setSuperscript();
//            }
//        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

//        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setHeading(4);
//            }
//        });
//
//        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setHeading(5);
//            }
//        });
//
//        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setHeading(6);
//            }
//        });

//        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
//            private boolean isChanged;
//
//            @Override
//            public void onClick(View v) {
//                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
//                isChanged = !isChanged;
//            }
//        });

//        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
//            private boolean isChanged;
//
//            @Override
//            public void onClick(View v) {
//                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
//                isChanged = !isChanged;
//            }
//        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

//        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setBlockquote();
//            }
//        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

//        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
//                        "dachshund", 320);
//            }
//        });
//
//        findViewById(R.id.action_insert_youtube).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.insertYoutubeVideo("https://www.youtube.com/embed/pS5peqApgUA");
//            }
//        });
//
//        findViewById(R.id.action_insert_audio).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3");
//            }
//        });
//
//        findViewById(R.id.action_insert_video).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360);
//            }
//        });

//        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
//            }
//        });
//        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.insertTodo();
//            }
//        });





























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

        mEditor = findViewById(R.id.editContent2);
        mEditor.setHtml(note.content);

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
