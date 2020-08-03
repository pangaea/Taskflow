package com.pangaea.taskflow.ui.shared;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.TaskflowApp;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.notes.NoteActivity;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemsViewModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class ItemsFragment extends Fragment {
    protected void navigateToItemActivity(Context context, Class<?> clazz) {
        navigateToItemActivity(context, clazz, null);
    }
    protected void navigateToItemActivity(Context context, Class<?> clazz, Integer id) {
        Intent intent = new Intent(context, clazz);
        if(id != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            intent.putExtras(bundle);
        }
        else{
            //Integer project_id = ((TaskflowApp)getActivity().getApplication()).getCurrentProjectId(getActivity());
            Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();
            if(project_id != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("project_id", project_id);
                intent.putExtras(bundle);
            }
        }
        startActivity(intent);
    }

    protected void displayProjectInTitlebar(ItemsViewModel model) {
        //Integer project_id = ((TaskflowApp)getActivity().getApplication()).getCurrentProjectId(getActivity());
        Integer project_id = ((BaseActivity)getActivity()).getCurrentProjectId();
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
}
