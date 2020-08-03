package com.pangaea.taskflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public Integer getCurrentProjectId(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            return bundle.getInt("project_id");
        }
        return null;
    }

    public void setCurrentProjectId(Integer project_id){
        Intent intent = getIntent();
        if(project_id != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("project_id", project_id);
            intent.putExtras(bundle);
        }
        else{
            intent.removeExtra("project_id");
        }
    }

    public void navigateToProjectHome(Integer project_id) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("project_id", project_id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
