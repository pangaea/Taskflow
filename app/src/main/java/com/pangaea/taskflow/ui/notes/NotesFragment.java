package com.pangaea.taskflow.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.ui.notes.adapters.NotesAdapter;
import com.pangaea.taskflow.ui.notes.viewmodels.NotesViewModel;
import com.pangaea.taskflow.ui.shared.ItemsFragment;
import com.pangaea.taskflow.ui.shared.adapters.AutoCompleteSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class NotesFragment extends ItemsFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        final ListView lv = view.findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle list select
                Note note = (Note)lv.getAdapter().getItem(position);
                navigateToItemActivity(getActivity(), NoteActivity.class, note.id);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.button_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToItemActivity(getActivity(), NoteActivity.class);
            }
        });

        final NotesViewModel model = ViewModelProviders.of(this).get(NotesViewModel.class);

        //////////////////////////////////
        setupToolbar(getActivity(), view, null, false,
                o -> {
                    subscribeToModel(model, view);
                });
        /////////////////////////////////////////////////////

        subscribeToModel(model, view);
        return view;
    }

    private void subscribeToModel(final NotesViewModel model, final View view) {
        Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();

        // Get 'sortBy' from dropdown
        AutoCompleteTextView sortSpinner = view.findViewById(R.id.sort_spinner);
        String sortBy = AutoCompleteSpinnerAdapter.getSelectedSpinnerValue(sortSpinner);

        LiveData<List<Note>> ldNotes = (project_id != null) ?
                model.getNotesByProject(project_id) : model.getGlobalNotes();
        ldNotes.observe(this.getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> data) {
                // Sort here instead of db?
                if (sortBy.equals("NAME")) {
                    data = data.stream().sorted((o1, o2) -> o1.title.compareTo(o2.title))
                            .collect(Collectors.toList());
                } else if (sortBy.equals("CREATED")) {
                    data = data.stream().sorted((o1, o2) -> Math.negateExact(o1.createdAt.compareTo(o2.createdAt)))
                            .collect(Collectors.toList());
                } else if (sortBy.equals("MODIFIED")) {
                    data = data.stream().sorted((o1, o2) -> Math.negateExact(o1.modifiedAt.compareTo(o2.modifiedAt)))
                            .collect(Collectors.toList());
                }
                ListView lv = view.findViewById(R.id.listView);
                NotesAdapter adapter = new NotesAdapter(getActivity().getApplicationContext(), (ArrayList)data);
                lv.setAdapter(adapter);
            }
        });

        displayProjectInTitlebar(model);
    }
}
