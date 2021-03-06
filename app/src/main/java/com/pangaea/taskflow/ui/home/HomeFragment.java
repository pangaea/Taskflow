package com.pangaea.taskflow.ui.home;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.Note;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;
import com.pangaea.taskflow.ui.home.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private HomeViewModel homeViewModel;
    //SharedPreferences preferences;
    static String noSelection = TaskflowApp.getRes().getString(R.string.no_project_selected_list);

    @Override
    public void onStart() {
        super.onStart();

        // Set home screen color preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int colorPrefProject = preferences.getInt("color_pref_project", getResources().getColor(R.color.color_pref_project_default));
        int colorPrefTasks = preferences.getInt("color_pref_tasks", getResources().getColor(R.color.color_pref_tasks_default));
        int colorPrefNotes = preferences.getInt("color_pref_notes", getResources().getColor(R.color.color_pref_notes_default));
        int colorPrefLists = preferences.getInt("color_pref_lists", getResources().getColor(R.color.color_pref_lists_default));
        LinearLayout projectView = view.findViewById(R.id.project_summary_layout);
        projectView.setBackgroundColor(colorPrefProject);
        TextView tasksView = view.findViewById(R.id.tasks_summary);
        tasksView.setBackgroundColor(colorPrefTasks);
        TextView notesView = view.findViewById(R.id.notes_summary);
        notesView.setBackgroundColor(colorPrefNotes);
        TextView listsView = view.findViewById(R.id.lists_summary);
        listsView.setBackgroundColor(colorPrefLists);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        final Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();

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
                        new ArrayAdapter<>(getActivity(), R.layout.project_spinner_item, z);

                adapter.setDropDownViewResource(R.layout.project_spinner_dropdown_item);
                AutoCompleteTextView projectSpinner = getActivity().findViewById(R.id.project_spinner);
                projectSpinner.setAdapter(adapter);

                if (project_id != null) {
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
                                ((BaseActivity)getActivity()).setCurrentProjectId(p.id);
                                Navigation.findNavController(view).navigate(R.id.nav_home);
                                return;
                            }
                        }

                        // On selection of none - wipe out project state
                        ((BaseActivity)getActivity()).setCurrentProjectId(null);
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

        LiveData<List<Note>> ldNotes = (project_id != null) ? homeViewModel.getNotesByProject(project_id) : homeViewModel.getGlobalNotes();
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

        LiveData<List<Checklist>> ldChecklists = (project_id != null) ? homeViewModel.getChecklistsByProject(project_id) : homeViewModel.getGlobalChecklists();
        ldChecklists.observe(this.getViewLifecycleOwner(), new Observer<List<Checklist>>() {
            @Override
            public void onChanged(@Nullable List<Checklist> data) {
                TextView textView = view.findViewById(R.id.lists_summary);
                String strListFormat = getResources().getString(R.string.checklists_summary_text, data.size());
                Spanned sp = Html.fromHtml(strListFormat);
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

        LiveData<List<Task>> ldTasks = (project_id != null) ? homeViewModel.getTasksByProject(project_id) : homeViewModel.getGlobalTasks();
        ldTasks.observe(this.getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> data) {
                int activeTasks = 0, completedTasks = 0;
                for(int i = 0; i < data.size(); i++) {
                    Task task = data.get(i);
                    if( task.status == TaskStatus.ACTIVE ) activeTasks++;
                    if( task.status == TaskStatus.COMPLETED ) completedTasks++;
                }
                TextView textView = view.findViewById(R.id.tasks_summary);
                String strTaskFormat = getResources().getString(R.string.tasks_summary_text, data.size(), activeTasks, completedTasks);
                Spanned sp = Html.fromHtml(strTaskFormat, Html.FROM_HTML_MODE_COMPACT);
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