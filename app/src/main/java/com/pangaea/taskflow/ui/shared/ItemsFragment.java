package com.pangaea.taskflow.ui.shared;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pangaea.taskflow.ui.notes.NoteActivity;

import androidx.fragment.app.Fragment;

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
            Integer project_id = getProjectId();
            if(project_id != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("project_id", getProjectId());
                intent.putExtras(bundle);
            }
        }
        startActivity(intent);
    }
    protected Integer getProjectId(){
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            return bundle.getInt("project_id");
        }
        return null;
    }
}
