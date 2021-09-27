package com.pangaea.taskflow.ui.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.notes.viewmodels.NoteViewModel;
import com.pangaea.taskflow.ui.notes.viewmodels.NotesViewModel;
import com.pangaea.taskflow.ui.projects.viewmodels.ProjectViewModel;
import com.pangaea.taskflow.ui.shared.ItemActivity;
import com.pangaea.taskflow.ui.shared.ProjectAssociatedItemActivity;

import java.util.Date;
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
        mEditor.setPlaceholder(getResources().getString(R.string.note_content_placeholder_label));

        mContent = findViewById(R.id.editContent);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                mContent.setText(text);
            }
        });

        mEditor.setOnDecorationChangeListener((String text, List<RichEditor.Type> types) -> {
            adjustButtonState(types, RichEditor.Type.BOLD, R.id.action_bold);
            adjustButtonState(types, RichEditor.Type.ITALIC, R.id.action_italic);
            adjustButtonState(types, RichEditor.Type.STRIKETHROUGH, R.id.action_strikethrough);
            adjustButtonState(types, RichEditor.Type.UNDERLINE, R.id.action_underline);
            adjustButtonState(types, RichEditor.Type.H1, R.id.action_heading1);
            adjustButtonState(types, RichEditor.Type.H2, R.id.action_heading2);
            adjustButtonState(types, RichEditor.Type.H3, R.id.action_heading3);
            adjustButtonState(types, RichEditor.Type.JUSTIFYLEFT, R.id.action_align_left);
            adjustButtonState(types, RichEditor.Type.JUSTIFYCENTER, R.id.action_align_center);
            adjustButtonState(types, RichEditor.Type.JUSTIFYRIGHT, R.id.action_align_right);
            adjustButtonState(types, RichEditor.Type.UNORDEREDLIST, R.id.action_insert_bullets);
            adjustButtonState(types, RichEditor.Type.ORDEREDLIST, R.id.action_insert_numbers);
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
                toggleButtonState(R.id.action_bold);
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_italic);
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_strikethrough);
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_underline);
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_heading1);
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_heading2);
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_heading3);
                mEditor.setHeading(3);
            }
        });

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
                toggleButtonState(R.id.action_align_left);
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_align_center);
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_align_right);
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_insert_bullets);
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonState(R.id.action_insert_numbers);
                mEditor.setNumbers();
            }
        });


        // Project assign ///////////////////////////////////////////
        populateProjects(model.getProjects(), new ProjectsLoadedCallback() {
            @Override
            public void projectsLoaded() {
                subscribeToModel(model);
            }
        });

        if (noteId == -1)
            attachDirtyEvents(R.id.editTitle, R.id.editContent, R.id.project_spinner);
    }

    private void adjustButtonState(List<RichEditor.Type> types, RichEditor.Type type, int resId) {
        if (types.contains(type)) {
            findViewById(resId).setBackgroundColor(Color.GRAY);
        } else {
            findViewById(resId).setBackgroundColor(Color.BLACK);
        }
    }

    private void toggleButtonState(int resId) {
        ImageButton boldBtn = findViewById(resId);
        ColorDrawable buttonColor = (ColorDrawable) boldBtn.getBackground();
        if(buttonColor.getColor()==Color.GRAY) {
            boldBtn.setBackgroundColor(Color.BLACK);
        } else {
            boldBtn.setBackgroundColor(Color.GRAY);
        }
    }

    @Override public void initNewItem() {
        // Initialize new item here
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
        if(noteId > 0) {
            note.id = noteId;
        }
        // Project assign ///////////////////////////////////////////
        note.project_id = getProjectId();
        return note;
    }
    @Override public String deleteWarning(){
        TextView tvTitle = findViewById(R.id.editTitle);
        String delMsg = getResources().getString(R.string.note_delete_conformation_label);
        return delMsg.replace("%1", tvTitle.getText().toString());
    }
}
