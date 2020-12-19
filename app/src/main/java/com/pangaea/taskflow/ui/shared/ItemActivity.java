package com.pangaea.taskflow.ui.shared;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.pangaea.taskflow.BaseActivity;
import com.pangaea.taskflow.R;
import com.pangaea.taskflow.ui.shared.viewmodels.ItemViewModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public abstract class ItemActivity<M, VM extends ItemViewModel> extends BaseActivity {
    private AppCompatActivity me = this;
    private VM itemModel = null;
    private int _itemId = -2;
    private MenuItem itemSave = null;

    protected int getItemId() {
        if( _itemId < -1 ) {
            // Hacky: First time call - init
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                return bundle.getInt("id", -1);
            }
            else{
                _itemId = -1;
            }
        }
        return _itemId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                if(itemSave != null && itemSave.isEnabled()) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(me);
                    builder1.setMessage("Would you like to save your changes?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    saveItem();
                                    onBackPressed();
                                }
                            });
                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    onBackPressed();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{
                    onBackPressed();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveItem(){
        final int itemId = getItemId();
        M item = buildModel();
        if(itemId > 0) {
            itemModel.update(item);
        }
        else{
            itemModel.insert(item);
        }
        setSaveEnabled(false);
        Toast.makeText(me, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void setSaveEnabled(boolean enabled) {
        if(itemSave == null) return;
        Drawable resIcon = getResources().getDrawable(android.R.drawable.ic_menu_save);
        if (!enabled)
            resIcon.mutate().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        itemSave.setEnabled(enabled); // any text will be automatically disabled
        itemSave.setIcon(resIcon);
    }

    protected void attachDirtyEvents(int ... ids){
        for (int id: ids) {
            TextView tvName = findViewById(id);
            tvName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    setSaveEnabled(true);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_actions, menu);

        final int itemId = getItemId();
        itemSave = menu.findItem(R.id.item_save);
        itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Save current task content
                saveItem();
                return true;
            }
        });
        setSaveEnabled(false);

        MenuItem itemDelete = menu.findItem(R.id.item_delete);
        if(itemId < 0) {
            itemDelete.setEnabled(false);
            itemDelete.setVisible(false);
        }
        else {
            Drawable resIcon = getResources().getDrawable(android.R.drawable.ic_menu_delete);
            resIcon.mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            itemDelete.setIcon(resIcon);

            itemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(me);
                    builder1.setMessage(deleteWarning());
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    M item = buildModel();
                                    if (itemId > 0) {
                                        itemModel.delete(item);
                                    }
                                    Toast.makeText(me, "Deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return true;
                }
            });
        }

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
                    if( data.size() > 0) {
                        M item = data.get(0);
                        fillFields(item);
                        setSaveEnabled(false);
                    }
                }
            });
        } else {
            initNewItem();
        }
    }
}
