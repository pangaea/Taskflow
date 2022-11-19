package com.pangaea.taskflow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.pangaea.taskflow.state.db.entities.Project;
import com.pangaea.taskflow.ui.home.viewmodels.HomeViewModel;
import com.pangaea.taskflow.ui.projects.viewmodels.ProjectsViewModel;
import com.pangaea.taskflow.ui.settings.SettingsActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ProjectsViewModel projectsViewModel;
    static String noSelection = TaskflowApp.getRes().getString(R.string.no_project_selected_list);
    //private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int colorPrefProject = preferences.getInt("color_pref_project", getResources().getColor(R.color.color_pref_project_default));
        //LinearLayout mainView = findViewById(R.id.project_summary_layout);
        //mainView.setBackgroundColor(colorPrefProject);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_notes, R.id.nav_checklists, R.id.nav_tasks,
                R.id.nav_projects)
                .setOpenableLayout(drawer)
                //.setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        final Context self = this;
        final Integer project_id = getCurrentProjectId();
        projectsViewModel = ViewModelProviders.of(this).get(ProjectsViewModel.class);
        projectsViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
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
                        new ArrayAdapter<>(self, R.layout.project_spinner_item, z);

                adapter.setDropDownViewResource(R.layout.project_spinner_dropdown_item);
                AutoCompleteTextView projectSpinner = findViewById(R.id.project_spinner);
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
                                setCurrentProjectId(p.id);
                                //navController.navigate(R.id.nav_home);
                                refreshCurrentFragment(navController);
                                return;
                            }
                        }

                        // On selection of none - wipe out project state
                        setCurrentProjectId(null);
                        //navController.navigate(R.id.nav_home);
                        refreshCurrentFragment(navController);
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
    }

    private void refreshCurrentFragment(NavController navController){
        NavDestination id = navController.getCurrentDestination();
        navController.popBackStack(id.getId(), true);
        navController.navigate(id.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavDestination x = navController.getCurrentDestination();
        if(x.getId() == R.id.nav_home) {
            finish();
        }
        else {
            navController.popBackStack();
            navController.navigate(R.id.nav_home);
        }
    }
}
