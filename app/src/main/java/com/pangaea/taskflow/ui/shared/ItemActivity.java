package com.pangaea.taskflow.ui.shared;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.BaseEntity;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public abstract class ItemActivity<M, VM extends ItemViewModel> extends BaseActivity {
    private AppCompatActivity me = this;
    private VM itemModel = null;
    private MenuItem itemSave = null;

    protected int getItemId() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            return bundle.getInt("id", -1);
        }
        return -1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Timer timer = new Timer();
    private final long DELAY = 1000; // Milliseconds
    protected void saveItem(){

        if (itemModel == null) {
            return;
        }

        if(getItemId() > 0) {
            synchronized (this) {
                timer.cancel();
            }
            timer = new Timer();
            timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (ItemActivity.class) {
                            itemModel.update(buildModel());
                        }
                    }
                },
                DELAY
            );
        }
    }

    protected void attachDirtyEvents(@NonNull int ... ids){
        for (int id: ids) {
            TextView tvName = findViewById(id);
            tvName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    //Log.d("STATE", "afterTextChanged. ...........................");
                    //setSaveEnabled(true);
                    saveItem();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_actions, menu);

        final int itemId = getItemId();

        MenuItem itemDelete = menu.findItem(R.id.item_delete);
        Drawable resIcon = getResources().getDrawable(android.R.drawable.ic_menu_delete);
        resIcon.mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        itemDelete.setIcon(resIcon);

        itemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(me);
                deleteAlertBuilder.setMessage(deleteWarning());
                deleteAlertBuilder.setCancelable(true);
                deleteAlertBuilder.setPositiveButton(
                        getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                M item = buildModel();
                                if (itemId > 0) {
                                    itemModel.delete(item);
                                }
                                Toast.makeText(me, getResources().getString(R.string.deleted_confirmation), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                deleteAlertBuilder.setNegativeButton(
                        getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog deleteAlert = deleteAlertBuilder.create();
                deleteAlert.show();
                return true;
            }
        });

        return true;
    }

    abstract public void initNewItem();
    abstract public void fillFields(M item);
    abstract public M buildModel();
    abstract public String deleteWarning();

    protected void subscribeToModel(final VM model) {
        itemModel = model;
        int checklistId = getItemId();
        if(checklistId > 0) {
            // Load item information
            model.getModel().observe(this, new Observer<List<M>>() {
                @Override
                public void onChanged(@Nullable List<M> data) {
                    if (data.size() > 0) {
                        fillFields(data.get(0));
                        model.getModel().removeObserver(this);
                    }
                }
            });
        } else {
            initNewItem();
        }
    }
}
