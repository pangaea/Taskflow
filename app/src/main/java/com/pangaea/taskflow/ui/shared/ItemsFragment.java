package com.pangaea.taskflow.ui.shared;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.state.db.entities.Task;
import com.pangaea.taskflow.state.db.entities.enums.TaskStatus;
import com.pangaea.taskflow.ui.shared.adapters.AutoCompleteSpinnerAdapter;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemsViewModel;
import com.pangaea.taskflow.ui.tasks.TaskActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

public class ItemsFragment extends Fragment {
    protected void navigateToItemActivity(Context context, Class<?> clazz) {
        navigateToItemActivity(context, clazz, null);
    }

    protected void launchCreateModal(int title, int field, Consumer<String> callback) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle(getResources().getString(title));
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(field);
        input.setPaddingRelative(60, 20, 60, 20);
        alertBuilder.setView(input);
        alertBuilder.setPositiveButton(getResources().getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.accept(input.getText().toString());
                    }
                });
        alertBuilder.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertBuilder.show();
    }
    protected void navigateToItemActivity(Context context, Class<?> clazz, Integer id) {
        Intent intent = new Intent(context, clazz);
        if(id != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
//        else{
//            Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();
//            if(project_id != null) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("project_id", project_id);
//                intent.putExtras(bundle);
//            }
//        }
//        startActivity(intent);
    }

    protected void displayProjectInTitlebar(ItemsViewModel model) {
        // Re-show project picket
        com.google.android.material.textfield.TextInputLayout projectPickerLayout =
                getActivity().findViewById(R.id.project_picker_layout);
        projectPickerLayout.setVisibility(View.VISIBLE);

        Integer project_id = ((BaseActivity) getActivity()).getCurrentProjectId();
        if (project_id != null) {
            model.getProject(project_id).observe(this.getViewLifecycleOwner(), new Observer<List<Project>>() {
                @Override
                public void onChanged(@Nullable List<Project> data) {
                    Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                    CharSequence curTitle = toolbar.getTitle();
                    toolbar.setTitle(curTitle + ": " + data.get(0).name);
                }
            });
        }
    }

    protected void setupToolbar(FragmentActivity frag, View view, List<Pair<String, String>> filters,
                                boolean taskList, Consumer<String> callback) {

        List<Pair<String, String>> sortOptions = new ArrayList<Pair<String, String>>();
        sortOptions.add(new Pair<String, String>("MODIFIED", getResources().getString(R.string.SortBy_Modified)));
        sortOptions.add(new Pair<String, String>("CREATED", getResources().getString(R.string.SortBy_Created)));
        sortOptions.add(new Pair<String, String>("NAME", getResources().getString(R.string.SortBy_Name)));
        if (taskList == true) {
            sortOptions.add(new Pair<String, String>("STATUS", getResources().getString(R.string.SortBy_Status)));
        }
        AutoCompleteSpinnerAdapter sortAdapter = new AutoCompleteSpinnerAdapter(getContext(), R.layout.project_spinner_item,
                sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AutoCompleteTextView sortSpinner = view.findViewById(R.id.sort_spinner);
        sortSpinner.setAdapter(sortAdapter);
        AutoCompleteSpinnerAdapter.setSelectedPosition(sortSpinner, 0);
        AutoCompleteSpinnerAdapter.setUpdateListener(sortSpinner, o -> {
            callback.accept(o);
        });

        if (filters != null) {
            AutoCompleteSpinnerAdapter filterAdapter = new AutoCompleteSpinnerAdapter(getContext(), R.layout.project_spinner_item,
                    filters);
            filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            AutoCompleteTextView filterSpinner = view.findViewById(R.id.filter_spinner);
            filterSpinner.setAdapter(filterAdapter);
            AutoCompleteSpinnerAdapter.setSelectedValue(filterSpinner, "NONE");
            AutoCompleteSpinnerAdapter.setUpdateListener(filterSpinner, o -> {
                callback.accept(o);
            });
        } else {
            TextInputLayout filterSpinnerWrapper = view.findViewById(R.id.filter_spinner_wrapper);
            filterSpinnerWrapper.setVisibility(View.GONE);
        }
    }
}
