package com.pangaea.taskflow.ui.checklists;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.Checklist;
import com.pangaea.taskflow.state.db.entities.ChecklistItem;
import com.pangaea.taskflow.state.db.entities.ChecklistWithItems;
import com.pangaea.taskflow.ui.checklists.adapters.ChecklistItemTouchHelperCallback;
import com.pangaea.taskflow.ui.checklists.adapters.ChecklistItemsAdapter;
import com.pangaea.taskflow.ui.checklists.adapters.OnStartDragListener;
import com.pangaea.taskflow.ui.checklists.viewmodels.ChecklistViewModel;
import com.pangaea.taskflow.ui.shared.ItemActivity;
import com.pangaea.taskflow.ui.shared.ProjectAssociatedItemActivity;

import java.util.ArrayList;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistActivity extends ProjectAssociatedItemActivity<ChecklistWithItems, ChecklistViewModel>
                    implements OnStartDragListener {

    private final OnStartDragListener self = this;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Extract note id
        final int checklistId = getItemId();

        ChecklistViewModel.Factory factory = new ChecklistViewModel.Factory(
                getApplication(), checklistId);

        final ChecklistViewModel model = new ViewModelProvider(this, (ViewModelProvider.Factory) factory)
                .get(ChecklistViewModel.class);

        ChecklistItemsAdapter adapter = new ChecklistItemsAdapter(getApplicationContext(), new ArrayList<ChecklistItem>(), self);
        RecyclerView recyclerView = findViewById(R.id.listItemsView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ItemTouchHelper.Callback callback = new ChecklistItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        // Project assign ///////////////////////////////////////////
        populateProjects(model.getProjects(), new ProjectAssociatedItemActivity.ProjectsLoadedCallback(){
            @Override public void projectsLoaded(){
                subscribeToModel(model);
            }
        });

        final Button button = findViewById(R.id.button_new_item);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RecyclerView recyclerView = findViewById(R.id.listItemsView);
                ChecklistItemsAdapter adapter = (ChecklistItemsAdapter)recyclerView.getAdapter();
                adapter.addNewItem(new ChecklistItem("", false, checklistId, 0));
                recyclerView.invalidateItemDecorations();
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }
        });

        if(checklistId == -1)
            attachDirtyListeners();
    }

    private void attachDirtyListeners(){
        attachDirtyEvents(R.id.editName, R.id.editDescription, R.id.project_spinner);
    }

    @Override public void initNewItem() {
        // Initialize new item here
    }

    @Override
    public void fillFields(ChecklistWithItems checklist){
        TextView tvName = findViewById(R.id.editName);
        tvName.setText(checklist.checklist.name);
        TextView tvDescription = findViewById(R.id.editDescription);
        tvDescription.setText(checklist.checklist.description);

        RecyclerView recyclerView = findViewById(R.id.listItemsView);
        ChecklistItemsAdapter adapter = (ChecklistItemsAdapter)recyclerView.getAdapter();
        adapter.setItems(checklist.items);
        adapter.notifyDataSetChanged();
        // Project assign ///////////////////////////////////////////
        setProjectSelection(checklist.checklist.project_id);
        attachDirtyListeners();
    }

    @Override
    public ChecklistWithItems buildModel(){
        int checklistId = getItemId();
        TextView tvName = findViewById(R.id.editName);
        TextView tvDescription = findViewById(R.id.editDescription);
        Checklist checklist = new Checklist(tvName.getText().toString(), tvDescription.getText().toString());
        if(checklistId > 0) {
            checklist.id = checklistId;
        }

        // Project assign ///////////////////////////////////////////
        checklist.project_id = getProjectId();

        // Read in list items and reset the positions to match the ui
        RecyclerView recyclerView = findViewById(R.id.listItemsView);
        ChecklistItemsAdapter adapter = (ChecklistItemsAdapter)recyclerView.getAdapter();

        // Edit the position value of each item
        for (int i = 0; i < adapter.mItems.size(); i++) {
            adapter.mItems.get(i).position = i;
        }
        return new ChecklistWithItems(checklist, adapter.mItems);
    }

    @Override
    public String deleteWarning() {
        TextView tvName = findViewById(R.id.editName);
        String delMsg = getResources().getString(R.string.list_delete_conformation_label);
        return delMsg.replace("%1", tvName.getText().toString());
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemChanged(){
        setSaveEnabled(true);
    }
}
