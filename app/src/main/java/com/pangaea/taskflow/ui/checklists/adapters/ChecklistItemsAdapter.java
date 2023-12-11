package com.pangaea.taskflow.ui.checklists.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pangaea.taskflow.R;
import com.pangaea.taskflow.state.db.entities.ChecklistItem;
import com.pangaea.taskflow.ui.shared.ItemActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistItemsAdapter extends RecyclerView.Adapter<ChecklistItemsAdapter.ItemViewHolder>
        implements CheclklistItemTouchHelperAdapter {

    public List<ChecklistItem> mItems;

    private final OnStartDragListener mDragStartListener;

    public ChecklistItemsAdapter(Context context, List<ChecklistItem> checklist_items, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        setItems(checklist_items);
    }

    public void setItems(List<ChecklistItem> items){
        Collections.sort(items, new Comparator<ChecklistItem>(){
            public int compare(ChecklistItem obj1, ChecklistItem obj2) {
                // ## Ascending order
                return Integer.valueOf(obj1.position).compareTo(Integer.valueOf(obj2.position));
            }
        });
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        itemViewHolder.textView.requestFocus();
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.textView.setText(mItems.get(position).name);
        holder.textView.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after){ }
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged (Editable s){
                mItems.get(holder.getAdapterPosition()).name = s.toString();
                mDragStartListener.onItemChanged();
            }
        });

        holder.checkedView.setChecked(mItems.get(position).checked);
        holder.checkedView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               mItems.get(holder.getAdapterPosition()).checked = isChecked;
               mDragStartListener.onItemChanged();
           }
       });

        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        mDragStartListener.onItemChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        mDragStartListener.onItemChanged();
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addNewItem(ChecklistItem item){
        mItems.add(item);
        notifyItemInserted(mItems.size()+1);
        mDragStartListener.onItemChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ChecklistItemTouchHelperViewHolder {

        public final CheckBox checkedView;
        public final TextView textView;
        public final ImageView handleView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            checkedView = itemView.findViewById(R.id.chkIos);
            textView = itemView.findViewById(R.id.text);
            handleView = itemView.findViewById(R.id.handle);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
