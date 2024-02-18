package com.pangaea.taskflow.ui.checklists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.ChecklistWithItems;
import com.pangaea.taskflow.ui.checklists.adapters.ChecklistsAdapter;
import com.pangaea.taskflow.ui.checklists.viewmodels.ChecklistsViewModel;
import com.pangaea.taskflow.ui.shared.ItemsFragment;
import com.pangaea.taskflow.ui.shared.adapters.AutoCompleteSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChecklistsFragment extends ItemsFragment {

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
                Checklist checklist = (Checklist)lv.getAdapter().getItem(position);
                navigateToItemActivity(getActivity(), ChecklistActivity.class, checklist.id);
            }
        });

        final ChecklistsViewModel model = ViewModelProviders.of(this).get(ChecklistsViewModel.class);

        FloatingActionButton fab = view.findViewById(R.id.button_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateModal(R.string.list_create_title, R.string.list_title_label, name ->{
                    Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();
                    Checklist checklist = new Checklist(name, "", project_id);
                    ChecklistWithItems obj = new ChecklistWithItems(checklist, List.of());
                    model.insert(obj, o -> {
                        navigateToItemActivity(getActivity(), ChecklistActivity.class,
                                ((Long)o).intValue());
                    });
                });
            }
        });

        //////////////////////////////////
        setupToolbar(getActivity(), view, null, false,
                o -> {subscribeToModel(model, view);});
        /////////////////////////////////////////////////////

        subscribeToModel(model, view);
        return view;
    }

    private void subscribeToModel(final ChecklistsViewModel model, final View view) {
        Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();

        // Get 'sortBy' from dropdown
        AutoCompleteTextView sortSpinner = view.findViewById(R.id.sort_spinner);
        String sortBy = AutoCompleteSpinnerAdapter.getSelectedSpinnerValue(sortSpinner);

        LiveData<List<Checklist>> ldChecklists = (project_id != null) ? model.getChecklistsByProject(project_id) :
                model.getGlobalChecklists();
        ldChecklists.observe(this.getViewLifecycleOwner(), new Observer<List<Checklist>>() {
            @Override
            public void onChanged(@Nullable List<Checklist> data) {
                // Sort here instead of db?
                if (sortBy.equals("NAME")) {
                    data = data.stream().sorted((o1, o2) -> o1.name.compareTo(o2.name))
                            .collect(Collectors.toList());
                } else if (sortBy.equals("CREATED")) {
                    data = data.stream().sorted((o1, o2) -> Math.negateExact(o1.createdAt.compareTo(o2.createdAt)))
                            .collect(Collectors.toList());
                } else if (sortBy.equals("MODIFIED")) {
                    data = data.stream().sorted((o1, o2) -> Math.negateExact(o1.modifiedAt.compareTo(o2.modifiedAt)))
                            .collect(Collectors.toList());
                }
                ListView lv = view.findViewById(R.id.listView);
                ChecklistsAdapter adapter = new ChecklistsAdapter(getActivity().getApplicationContext(), (ArrayList)data);
                lv.setAdapter(adapter);
            }
        });
        displayProjectInTitlebar(model);
    }
}