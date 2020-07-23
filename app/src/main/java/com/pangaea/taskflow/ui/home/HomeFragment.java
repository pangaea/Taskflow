package com.pangaea.taskflow.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.pangaea.taskflow.MainActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;
import com.pangaea.taskflow.ui.home.viewmodels.HomeViewModel;
import com.pangaea.taskflow.ui.notes.adapters.NotesAdapter;
import com.pangaea.taskflow.ui.tasks.TasksFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    //final List<Project> projects = new ArrayList();
    static String noSelection = "--None--";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getProjects().observe(this.getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> data) {
                // Insert projects into dropdown
                final List<Project> projects = new ArrayList();
                List<String> z = new ArrayList();
                z.add(noSelection);
                for (Project p : data) {
                    projects.add(p);
                    z.add(p.name);
                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, z);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                AutoCompleteTextView projectSpinner = getActivity().findViewById(R.id.project_spinner);
                projectSpinner.setAdapter(adapter);

                int project_id = -1;
                Intent intent = getActivity().getIntent();
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    project_id = bundle.getInt("project_id", -1);
                }

                if (project_id > 0) {
                    for (int i = 0; i < projects.size(); i++) {
                        Project p = projects.get(i);
                        if (p.id == project_id) {
                            projectSpinner.setText(p.name, false);
                            break;
                        }
                    }
                } else {
                    projectSpinner.setText(noSelection, false);
                }

                projectSpinner.addTextChangedListener( new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String pName = s.toString();
                        for (int ii = 0; ii < projects.size(); ii++) {
                            Project p = projects.get(ii);
                            if (p.name.equals(pName)) {
                                // On selection of project in list - set project state
                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("project_id", p.id);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                return;
                            }
                        }

                        // On selection of none - wipe out project state
                        Intent intent = getActivity().getIntent();
                        intent.removeExtra("project_id");
                        Navigation.findNavController(view).navigate(R.id.nav_home);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        });






        int project_id = -1;
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            project_id = bundle.getInt("project_id");
        }
/*
        if (project_id > 0) {
            homeViewModel.getProject(project_id).observe(this.getViewLifecycleOwner(), new Observer<List<Project>>() {
                @Override
                public void onChanged(@Nullable List<Project> data) {
                TextView projectView = view.findViewById(R.id.project_summary);
                //projectView.setVisibility(View.VISIBLE);
                projectView.setText(data.get(0).name);

                Button fab = getActivity().findViewById(R.id.button_null_project_home);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getActivity().getIntent();
                        intent.removeExtra("project_id");
                        Navigation.findNavController(view).navigate(R.id.nav_home);
                    }
                });
                }
            });
        }
        else{
            //LinearLayout projectLayout = view.findViewById(R.id.project_summary_layout);
            //projectLayout.setVisibility(View.GONE);
            TextView projectView = view.findViewById(R.id.project_summary);
            projectView.setText("[NO PROJECT]");
        }
*/
        LiveData<List<Note>> ldNotes = (project_id > 0) ? homeViewModel.getNotesByProject(project_id) : homeViewModel.getGlobalNotes();
        ldNotes.observe(this.getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> data) {
                TextView textView = view.findViewById(R.id.notes_summary);
                String strNoteFormat = getResources().getString(R.string.notes_summary_text, data.size());
                Spanned sp = Html.fromHtml(strNoteFormat);
                textView.setText(sp);
                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        Navigation.findNavController(view).navigate(R.id.nav_notes);
                        return false;
                    }
                });
            }
        });

        LiveData<List<Checklist>> ldChecklists = (project_id > 0) ? homeViewModel.getChecklistsByProject(project_id) : homeViewModel.getGlobalChecklists();
        ldChecklists.observe(this.getViewLifecycleOwner(), new Observer<List<Checklist>>() {
            @Override
            public void onChanged(@Nullable List<Checklist> data) {
                TextView textView = view.findViewById(R.id.lists_summary);
                String strNoteFormat = getResources().getString(R.string.checklists_summary_text, data.size());
                Spanned sp = Html.fromHtml(strNoteFormat);
                textView.setText(sp);
                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        Navigation.findNavController(view).navigate(R.id.nav_checklists);
                        return false;
                    }
                });
            }
        });

        LiveData<List<Task>> ldTasks = (project_id > 0) ? homeViewModel.getTasksByProject(project_id) : homeViewModel.getGlobalTasks();
        ldTasks.observe(this.getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> data) {
                int completedTasks = 0;
                for(int i = 0; i < data.size(); i++) {
                    Task task = data.get(i);
                    if( task.status == TaskStatus.COMPLETED ) completedTasks++;
                }
                TextView textView = view.findViewById(R.id.tasks_summary);
                String strNoteFormat = getResources().getString(R.string.tasks_summary_text, data.size(), completedTasks);
                Spanned sp = Html.fromHtml(strNoteFormat);
                textView.setText(sp);
                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        Navigation.findNavController(view).navigate(R.id.nav_tasks);
                        return false;
                    }
                });
            }
        });

        return view;
    }

    /**
     * Create a new instance of HomeFragment, specific to a project
     */
    public static HomeFragment newInstance(int project_id) {
        HomeFragment f = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("project_id", project_id);
        f.setArguments(args);
        return f;
    }
}